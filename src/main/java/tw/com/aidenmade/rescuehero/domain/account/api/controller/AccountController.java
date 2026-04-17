package tw.com.aidenmade.rescuehero.domain.account.api.controller;

import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tw.com.aidenmade.rescuehero.domain.account.api.request.*;
import tw.com.aidenmade.rescuehero.domain.account.application.dto.AccountDto;
import tw.com.aidenmade.rescuehero.domain.account.application.service.AccountService;
import tw.com.aidenmade.rescuehero.domain.account.application.service.CaptchaService;
import tw.com.aidenmade.rescuehero.domain.base.api.controller.AbstractBaseController;
import tw.com.aidenmade.rescuehero.utils.EmailUtils;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "帳號管理", description = "處理用戶帳號註冊、查詢、更新等相關 API")
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController extends AbstractBaseController {
    private final AccountService accountService;
    private final CaptchaService captchaService;
    // 安全特殊符號
    private final String SAFE_SYMBOLS = "!@#$%^&*";
    private final String RISKY_SYMBOLS = "\"'\\\\<>";

    @Operation(summary = "取得所有帳號", description = "分頁查詢所有帳號資訊（僅限管理員）")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查詢成功"),
            @ApiResponse(responseCode = "403", description = "權限不足")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Object> listAll(Pageable pageable) {
        return okResponse(accountService.listAll(pageable));
    }

    // 僅供練習用
    // 適用時機：已知帳號 ID，只需取得單一帳號資訊時使用。
    // 結果為單一物件或空（404），不需串流處理。
    @Operation(summary = "非阻塞取得單一帳號（Mono）", description = "以 WebFlux Mono 非阻塞查詢指定 ID 帳號，查無時回傳 404（需登入）")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查詢成功"),
            @ApiResponse(responseCode = "404", description = "帳號不存在"),
            @ApiResponse(responseCode = "401", description = "未授權")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/reactive/mono/{id}")
    public Mono<ResponseEntity<AccountDto>> getByIdMono(
            @Parameter(description = "帳號 ID") @PathVariable Long id) {
        return accountService.getByIdMono(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // 僅供練習用
    // 適用時機：資料量大或需要即時逐筆推送時使用（如即時監控、大量資料下載）。
    // 回應格式為 NDJSON（每筆帳號獨立一行），呼叫端需支援串流讀取。
    // 首筆資料可更快送達，伺服器記憶體壓力低於 Mono 版本。
    @Operation(summary = "非阻塞取得所有帳號（Flux）", description = "以 WebFlux Flux 串流回傳所有帳號，每筆帳號作為獨立事件推送（僅限管理員）")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查詢成功"),
            @ApiResponse(responseCode = "403", description = "權限不足")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/reactive/flux/accounts", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<AccountDto> listAllFlux() {
        return accountService.listAllFlux();
    }

    @Operation(summary = "取得帳號資訊", description = "根據帳號名稱查詢帳號資訊")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查詢成功"),
            @ApiResponse(responseCode = "404", description = "帳號不存在"),
            @ApiResponse(responseCode = "401", description = "未授權")
    })
    /**
     * 取得帳號資訊
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{username}")
    public ResponseEntity<Object> getByUsername(
            @Parameter(description = "帳號名稱") @PathVariable String username) {
        return accountService.getByUsername(username)
                .map(this::okResponse)
                .orElse(notFoundResponse());
    }

    @Operation(summary = "註冊帳號", description = "建立新帳號，需通過驗證碼驗證及密碼規則檢查")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "註冊成功"),
            @ApiResponse(responseCode = "400", description = "驗證碼錯誤、帳號密碼格式不符或帳號已存在")
    })
    /**
     * 建立帳號
     */
    @PermitAll
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody AccountCreateRequest request, HttpSession session) {
        Boolean verifyResult = captchaService.verifyCaptcha(request.getCaptcha(), session);
        if (verifyResult == null) {
            return errorResponseMsg("驗證碼逾期，驗證");
        } else if (!verifyResult) {
            return errorResponseMsg("驗證碼驗證");
        }

        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();

        List<String> errorMsgs = new ArrayList<>();
        // 檢查帳號、密碼不可為空或含空白
        if (StringUtils.isBlank(username)) {
            errorMsgs.add("帳號不可為空");
        } else {
            if (isSequential(username)) errorMsgs.add("帳號不可包含連續字串");
            if (username.contains(" ")) errorMsgs.add("帳號不可包含空白字元");
            if (username.length() > 20 || username.length() < 8) errorMsgs.add("帳號長度需在8~20之間");
            if (!username.matches("^[a-zA-Z0-9]+$")) errorMsgs.add("帳號僅可包含英數字，不可有特殊符號");
            if (accountService.getByUsername(username).isPresent()) errorMsgs.add("帳號已存在");
        }

        if (StringUtils.isBlank(password)) {
            errorMsgs.add("密碼不可為空");
        } else {
            if (isSequential(password)) errorMsgs.add("密碼不可包含連續字串");
            if (password.contains(" ")) errorMsgs.add("密碼不可包含空白字元");
            if (password.length() > 20 || password.length() < 8) errorMsgs.add("密碼長度需在8~20之間");
            if (password.matches(".*[" + RISKY_SYMBOLS + "].*")) errorMsgs.add("密碼不可包含有風險符號（\"'\\<>）");
            if (!password.matches(".*[A-Z].*")) errorMsgs.add("密碼需包含至少一個大寫字母");
            if (!password.matches(".*[a-z].*")) errorMsgs.add("密碼需包含至少一個小寫字母");
            if (!password.matches(".*[" + SAFE_SYMBOLS + "].*")) errorMsgs.add("密碼需包含至少一個安全特殊符號（!@#$%^&*）");
        }

        // 密碼不可與帳號或 email 類似
        String lowerPassword = password.toLowerCase();
        String lowerUsername = username.toLowerCase();
        String lowerEmail = email.toLowerCase();
        if (lowerPassword.contains(lowerUsername) || lowerUsername.contains(lowerPassword) ||
                lowerEmail.contains(lowerPassword)) {
            errorMsgs.add("密碼不可與帳號或 Email 類似");
        }

        if (StringUtils.isBlank(email)) {
            errorMsgs.add("Email 不可為空");
        } else {
            if (!EmailUtils.isValidEmailStrict(email)) errorMsgs.add("Email 格式不正確");
            if (accountService.getByEmail(email).isPresent()) errorMsgs.add("Email 已經被使用");
            //TODO 待補 mail server
        }

        if (!errorMsgs.isEmpty()) {
            return errorResponseMsg(errorMsgs.toArray(new String[0]));
        }
        ResponseEntity<Object> result = booleanToCommonResponse(accountService.register(request), "註冊");
        if (result.getStatusCode().is2xxSuccessful()) {
            captchaService.removeCaptcha(session);
        }
        return result;
        // 能不能寫成 fallback 的風格較好閱讀
    }

    @Operation(summary = "更新非敏感資訊", description = "更新帳號的非敏感資訊（如暱稱等）")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "更新成功"),
            @ApiResponse(responseCode = "400", description = "更新失敗"),
            @ApiResponse(responseCode = "401", description = "未授權")
    })
    /**
     * 更新非敏感資訊
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update/insensitive")
    public ResponseEntity<Object> updateInsensitive(@RequestBody AccountUpdateInsensitiveRequest request) {
        return booleanToCommonResponse(accountService.updateInsensitive(request), "變更使用者資訊");
    }

    @Operation(summary = "更新密碼", description = "變更帳號密碼")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "密碼變更成功"),
            @ApiResponse(responseCode = "400", description = "密碼變更失敗"),
            @ApiResponse(responseCode = "401", description = "未授權")
    })
    /**
     * 更新密碼
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update/password")
    public ResponseEntity<Object> updatePassword(@RequestBody AccountUpdatePasswordRequest request) {
        return booleanToCommonResponse(accountService.updatePassword(request), "變更密碼");
    }

    @Operation(summary = "更新管理員權限", description = "變更帳號的管理員權限（僅限管理員）")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "權限變更成功"),
            @ApiResponse(responseCode = "400", description = "權限變更失敗"),
            @ApiResponse(responseCode = "403", description = "權限不足")
    })
    /**
     * 更新管理帳號
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/is-admin")
    public ResponseEntity<Object> updateIsAdmin(@RequestBody AccountUpdateAdminRequest request) {
        return booleanToCommonResponse(accountService.updateIsAdmin(request), "變更管理帳號");
    }

    @Operation(summary = "停用帳號", description = "停用指定帳號")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "停用成功"),
            @ApiResponse(responseCode = "400", description = "停用失敗"),
            @ApiResponse(responseCode = "401", description = "未授權")
    })
    /**
     * 停用帳號
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update/is-active/inactive")
    public ResponseEntity<Object> inactive(@RequestBody AccountUpdateActiveRequest request) {
        return booleanToCommonResponse(accountService.updateIsActive(request, false), "停用帳號");
    }

    @Operation(summary = "啟用帳號", description = "啟用指定帳號（僅限管理員）")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "啟用成功"),
            @ApiResponse(responseCode = "400", description = "啟用失敗"),
            @ApiResponse(responseCode = "403", description = "權限不足")
    })
    /**
     * 啟用帳號
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/is-active/active")
    public ResponseEntity<Object> active(@RequestBody AccountUpdateActiveRequest request) {
        return booleanToCommonResponse(accountService.updateIsActive(request, true), "啟用帳號");
    }

    /**
     * 檢查是否為連續字串（簡單判斷英數字母遞增或遞減）
     */
    private boolean isSequential(String str) {
        if (str.length() < 3) return false;
        str = str.toLowerCase();
        for (int i = 0; i < str.length() - 2; i++) {
            int a = str.charAt(i), b = str.charAt(i+1), c = str.charAt(i+2);
            if ((b == a + 1 && c == b + 1) || (b == a - 1 && c == b - 1)) {
                return true;
            }
        }
        return false;
    }
}

