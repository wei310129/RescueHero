package tw.com.aidenmade.rescuehero.domain.account.api.controller;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tw.com.aidenmade.rescuehero.cache.JwtService;
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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController extends AbstractBaseController {
    private final AccountService accountService;
    private final JwtService jwtService;
    private final CaptchaService captchaService;

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

        setRefreshTokenIntoResponseCookie(response,
                jwtService.generateRefreshToken(accountDto.id(), accountDto.username(), accountDto.role().getUniquePattern()));

        ResponseEntity<Object> result = authorizedResponse(jwtService.generateAccessToken(accountDto.id(), accountDto.username(), accountDto.role().getUniquePattern()));
        if (result.getStatusCode().is2xxSuccessful()) {
            captchaService.removeCaptcha(session);
        }
        return result;
    }

    // refresh: 從 HttpOnly cookie 讀 refresh token，驗證後回傳新的 access（可選輪換 refresh）
    @PermitAll
    @PostMapping("/refresh")
    public ResponseEntity<Object> refreshToken(HttpServletRequest servletRequest, HttpServletResponse response) {
        Cookie[] cookies = servletRequest.getCookies();
        if (cookies == null) {
            return unauthorizedResponse("no refresh token");
        }
        String refreshToken = null;
        for (Cookie c : cookies) {
            if (CacheName.JWT_REFRESH_TOKEN.equals(c.getName())) {
                refreshToken = c.getValue();
                break;
            }
        }
        if (refreshToken == null || !jwtService.validateToken(refreshToken, CacheName.JWT_REFRESH_TOKEN)) {
            return unauthorizedResponse("invalid refresh token");
        }

        Long userId = jwtService.getUserIdFromToken(refreshToken);
        String username = jwtService.getUsernameFromToken(refreshToken);
        String roleUniquePattern = jwtService.getRoleUniquePatternFromToken(refreshToken);
        // 停用RefreshToken
        jwtService.invalidateAccessToken(JwtUtils.getJWTTokenByServletRequest(servletRequest));
        jwtService.invalidateRefreshToken(JwtUtils.getJWTTokenByServletRequest(servletRequest));

        // 輪換 refresh token（提高安全性）
        setRefreshTokenIntoResponseCookie(response, jwtService.generateRefreshToken(userId, username, roleUniquePattern));

        return authorizedResponse(jwtService.generateAccessToken(userId, username, roleUniquePattern));
    }

    private void setRefreshTokenIntoResponseCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshCookie = new Cookie(CacheName.JWT_REFRESH_TOKEN, refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true); // production: true（https）
        refreshCookie.setPath("/auth/refresh"); // 只有 refresh endpoint 可讀
        refreshCookie.setMaxAge(Math.toIntExact(CacheDefinition.JWT_REFRESH_TOKEN.getTtlType().getSeconds()));
        response.addCookie(refreshCookie);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest servletRequest) {
        jwtService.invalidateAccessToken(JwtUtils.getJWTTokenByServletRequest(servletRequest));
        jwtService.invalidateRefreshToken(JwtUtils.getJWTTokenByServletRequest(servletRequest));
        return successResponseMsg("登出");
    }

    @PermitAll
    @GetMapping("/captcha")
    public ResponseEntity<Object> getCaptcha(HttpSession session, HttpServletResponse response) {
        return booleanToCommonResponse(captchaService.getCaptcha(session, response), "驗證碼取得");
    }
}

