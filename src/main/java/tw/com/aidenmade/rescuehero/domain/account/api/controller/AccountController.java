package tw.com.aidenmade.rescuehero.domain.account.api.controller;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tw.com.aidenmade.rescuehero.domain.account.api.request.*;
import tw.com.aidenmade.rescuehero.domain.account.application.service.AccountService;
import tw.com.aidenmade.rescuehero.domain.account.application.service.CaptchaService;
import tw.com.aidenmade.rescuehero.domain.base.api.controller.AbstractBaseController;
import tw.com.aidenmade.rescuehero.utils.EmailUtils;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController extends AbstractBaseController {
    private final AccountService accountService;
    private final CaptchaService captchaService;
    // 安全特殊符號
    private final String SAFE_SYMBOLS = "!@#$%^&*";
    private final String RISKY_SYMBOLS = "\"'\\\\<>";

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Object> listAll(Pageable pageable) {
        return okResponse(accountService.listAll(pageable));
    }

    /**
     * 取得帳號資訊
     */
    @GetMapping("/{username}")
    public ResponseEntity<Object> getByUsername(@PathVariable String username) {
        return accountService.getByUsername(username)
                .map(this::okResponse)
                .orElse(notFoundResponse());
    }

    /**
     * 建立帳號
     */
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

    /**
     * 更新非敏感資訊
     */
    @PutMapping("/update/insensitive")
    public ResponseEntity<Object> updateInsensitive(@RequestBody AccountUpdateInsensitiveRequest request) {
        return booleanToCommonResponse(accountService.updateInsensitive(request), "變更使用者資訊");
    }

    /**
     * 更新密碼
     */
    @PutMapping("/update/password")
    public ResponseEntity<Object> updatePassword(@RequestBody AccountUpdatePasswordRequest request) {
        return booleanToCommonResponse(accountService.updatePassword(request), "變更密碼");
    }

    /**
     * 更新管理帳號
     */
    @PutMapping("/update/is-admin")
    public ResponseEntity<Object> updateIsAdmin(@RequestBody AccountUpdateAdminRequest request) {
        return booleanToCommonResponse(accountService.updateIsAdmin(request), "變更管理帳號");
    }

    /**
     * 停用帳號
     */
    @PutMapping("/update/is-active")
    public ResponseEntity<Object> updateIsActive(@RequestBody AccountUpdateActiveRequest request) {
        return booleanToCommonResponse(accountService.updateIsActive(request), "變更帳號狀態");
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

