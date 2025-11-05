package tw.com.aidenmade.rescuehero.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tw.com.aidenmade.rescuehero.cache.JwtService;
import tw.com.aidenmade.rescuehero.definition.cache.CacheName;
import tw.com.aidenmade.rescuehero.principal.UserPrincipal;
import tw.com.aidenmade.rescuehero.utils.JwtUtils;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info("有打到{}", request.getRequestURI());

        // 若沒帶 Authorization header，直接放行
        String authHeader = JwtUtils.getAuthorizationValue(request);
        if (authHeader == null || !authHeader.startsWith(JwtUtils.JWT_TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 驗證失敗立即返回 401，不繼續往下
        String token = JwtUtils.extractJWTToken(authHeader);
        if (!jwtService.validateToken(token, CacheName.JWT_ACCESS_TOKEN)) {
            log.warn("無效或過期的 JWT：{}", token);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 建立 Authentication
        UserPrincipal principal = new UserPrincipal(
                jwtService.getUserIdFromToken(token),
                jwtService.getUsernameFromToken(token),
                jwtService.getRoleUniquePatternFromToken(token)
        );
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            // 建立 principal 物件，並設定到 SecurityContext
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            principal.getAuthorities())
            );
        }

        filterChain.doFilter(request, response);
    }
}