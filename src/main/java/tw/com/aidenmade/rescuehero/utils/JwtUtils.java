package tw.com.aidenmade.rescuehero.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import tw.com.aidenmade.rescuehero.definition.cache.CacheName;

public class JwtUtils {

    public JwtUtils() {
        throw new AssertionError("No JwtUtils instances for you!");
    }

    /**
     * JWT token 前綴
     */
    public static String JWT_TOKEN_PREFIX = "Bearer ";

    /**
     * 取得 Header 中 Authorization 值
     * @return "Bearer TokenTokenToken"
     */
    public static String getAuthorizationValueFromHeader(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    /**
     * 取得 cookies 中 Authorization 值
     * @return "Bearer TokenTokenToken"
     */
    public static String getAuthorizationValueFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new RuntimeException("cookies is null");
        }
        String refreshToken = null;
        for (Cookie c : cookies) {
            if (CacheName.JWT_REFRESH_TOKEN.equals(c.getName())) {
                refreshToken = c.getValue();
                break;
            }
        }
        return refreshToken;
    }

    /**
     * 拆出 "Bearer " 之後的 token
     * @param authHeader "Bearer TokenTokenToken"
     * @return token "TokenTokenToken"
     */
    public static String extractJWTToken(String authHeader) {
        return authHeader.substring(7);
    }

    /**
     * 從 request header 取 access token
     */
    public static String getAccessTokenByRequestHeader(HttpServletRequest request) {
        return extractJWTToken(getAuthorizationValueFromHeader(request));
    }

    /**
     * 從 request cookies 取 refresh token
     */
    public static String getRefreshTokenByRequestCookies(HttpServletRequest request) {
        return getAuthorizationValueFromCookies(request);
    }
}
