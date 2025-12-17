package tw.com.aidenmade.rescuehero.domain.account.api.controller;

import io.jsonwebtoken.Claims;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tw.com.aidenmade.rescuehero.configuration.function.auth.JwtService;
import tw.com.aidenmade.rescuehero.definition.cache.CacheDefinition;
import tw.com.aidenmade.rescuehero.definition.cache.CacheName;
import tw.com.aidenmade.rescuehero.domain.account.api.request.AuthLoginRequest;
import tw.com.aidenmade.rescuehero.domain.account.application.dto.AccountDto;
import tw.com.aidenmade.rescuehero.domain.account.application.service.AccountService;
import tw.com.aidenmade.rescuehero.domain.account.application.service.CaptchaService;
import tw.com.aidenmade.rescuehero.domain.base.api.controller.AbstractBaseController;
import tw.com.aidenmade.rescuehero.utils.JwtUtils;
import tw.com.aidenmade.rescuehero.utils.PasswordUtils;

import java.util.Optional;

@Tag(name = "認證管理", description = "處理用戶登入、登出、Token 刷新及驗證碼相關 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController extends AbstractBaseController {
    private final AccountService accountService;
    private final JwtService jwtService;
    private final CaptchaService captchaService;

    @Operation(summary = "用戶登入", description = "驗證帳號密碼及驗證碼，成功後回傳 JWT Access Token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登入成功，回傳 Access Token"),
            @ApiResponse(responseCode = "401", description = "帳號不存在、密碼錯誤或帳號已停用"),
            @ApiResponse(responseCode = "400", description = "驗證碼錯誤或逾期")
    })
    @PermitAll
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthLoginRequest request, HttpSession session, HttpServletResponse response) {
        Boolean verifyResult = captchaService.verifyCaptcha(request.getCaptcha(), session);
        if (verifyResult == null) {
            return errorResponseMsg("驗證碼逾期，驗證");
        } else if (!verifyResult) {
            return errorResponseMsg("驗證碼驗證");
        }

        if (StringUtils.isBlank(request.getUsername()) || StringUtils.isBlank(request.getPassword())
                || StringUtils.isBlank(request.getCaptcha())) {
            return unauthorizedResponse("帳號、密碼及驗證碼為必填");
        }

        Optional<AccountDto> accountDtoOpt = accountService.getByUsername(request.getUsername());
        if (accountDtoOpt.isEmpty()) {
            return unauthorizedResponse("帳號不存在或密碼有誤");
        }
        AccountDto accountDto = accountDtoOpt.get();
        if (!accountDto.isActive()) return unauthorizedResponse("帳號已停用");
        if (!PasswordUtils.matches(request.getPassword(), accountDto.passwordHash())) return unauthorizedResponse("帳號不存在或密碼有誤");

        setRefreshTokenIntoResponseCookie(response, jwtService.generateRefreshToken(
                accountDto.id(), accountDto.username(), accountDto.role().roleType().name(), accountDto.role().name()));

        ResponseEntity<Object> result = authorizedResponse(jwtService.generateAccessToken(
                accountDto.id(), accountDto.username(), accountDto.role().roleType().name(), accountDto.role().name()));
        if (result.getStatusCode().is2xxSuccessful()) {
            captchaService.removeCaptcha(session);
        }
        return result;
    }

    @Operation(summary = "刷新 Token", description = "從 HttpOnly Cookie 讀取 Refresh Token，驗證後回傳新的 Access Token 並輪換 Refresh Token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "刷新成功，回傳新的 Access Token"),
            @ApiResponse(responseCode = "401", description = "Refresh Token 無效或已過期")
    })
    // refresh: 從 HttpOnly cookie 讀 refresh token，驗證後回傳新的 access（可選輪換 refresh）
    @PermitAll
    @PostMapping("/refresh")
    public ResponseEntity<Object> refreshToken(HttpServletRequest servletRequest, HttpServletResponse response) {
        String refreshToken;
        try {
            refreshToken = JwtUtils.getRefreshTokenByRequestCookies(servletRequest);
            if (!jwtService.validateToken(refreshToken, CacheName.JWT_REFRESH_TOKEN)) {
                throw new RuntimeException("invalid refresh token");
            }
        } catch (Exception e) {
            return unauthorizedResponse("validate fail: " + e.getMessage());
        }


        Claims claims = jwtService.extractClaimsFromToken(refreshToken);
        Long userId = jwtService.getUserIdByClaims(claims);
        String username = jwtService.getUsernameByClaims(claims);
        String roleType = jwtService.getRoleTypeByClaims(claims);
        String role = jwtService.getRoleByClaims(claims);
        // 停用RefreshToken
//        jwtService.invalidateAccessToken(JwtUtils.getJWTTokenByServletRequest(servletRequest));
        jwtService.invalidateRefreshToken(refreshToken);

        // 輪換 refresh token（提高安全性）
        setRefreshTokenIntoResponseCookie(response, jwtService.generateRefreshToken(userId, username, roleType, role));

        return authorizedResponse(jwtService.generateAccessToken(userId, username, roleType, role));
    }

    private void setRefreshTokenIntoResponseCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshCookie = new Cookie(CacheName.JWT_REFRESH_TOKEN, refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true); // production: true（https）
        refreshCookie.setPath("/api/auth/refresh"); // 只有 refresh endpoint 可讀
        refreshCookie.setMaxAge(Math.toIntExact(CacheDefinition.JWT_REFRESH_TOKEN.getTtlType().getSeconds()));
        response.addCookie(refreshCookie);
    }

    @Operation(summary = "用戶登出", description = "使 Access Token 及 Refresh Token 失效")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登出成功"),
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest servletRequest) {
        jwtService.invalidateAccessToken(JwtUtils.getAccessTokenByRequestHeader(servletRequest));
        jwtService.invalidateRefreshToken(JwtUtils.getRefreshTokenByRequestCookies(servletRequest));
        return successResponseMsg("登出");
    }

    @Operation(summary = "取得驗證碼", description = "產生圖形驗證碼並回傳")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "驗證碼取得成功"),
            @ApiResponse(responseCode = "500", description = "驗證碼產生失敗")
    })
    @PermitAll
    @GetMapping("/captcha")
    public ResponseEntity<Object> getCaptcha(HttpSession session, HttpServletResponse response) {
        return booleanToCommonResponse(captchaService.getCaptcha(session, response), "驗證碼取得");
    }
}

