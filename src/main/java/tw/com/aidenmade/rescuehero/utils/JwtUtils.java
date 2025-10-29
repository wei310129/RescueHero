package tw.com.aidenmade.rescuehero.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

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
    public static String getAuthorizationValue(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    /**
     * 拆出 "Bearer " 之後的 token
     * @param authHeader "Bearer TokenTokenToken"
     * @return token "TokenTokenToken"
     */
    public static String extractJWTToken(String authHeader) {
        return authHeader.substring(7);
    }

    public static String getJWTTokenByServletRequest(HttpServletRequest request) {
        return extractJWTToken(getAuthorizationValue(request));
    }
}
