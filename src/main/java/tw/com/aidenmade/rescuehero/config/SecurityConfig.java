package tw.com.aidenmade.rescuehero.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tw.com.aidenmade.rescuehero.filter.JwtAuthFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    // 1) Security chain dedicated to allowing WebSocket handshake endpoints without JWT filter.
    @Bean
    @Order(1)
    public SecurityFilterChain websocketFilterChain(HttpSecurity http) throws Exception {
        http
                // match the servlet context-path /api plus the websocket path
                .securityMatcher("/api/ws/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                );
        // Do NOT add jwtAuthFilter here so handshake can be anonymous
        return http.build();
    }

    // 2) Default security chain for the rest of the application (keeps JwtAuthFilter)
    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .csrf(AbstractHttpConfigurer::disable) // 本案使用 JWT
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers( // 免登入可使用的 api
//                                "/auth/login",
//                                "/auth/register",
//                                "/auth/captcha",
//                                "/actuator/health"
//                        )
                                .anyRequest()
                                .permitAll()
//                        .authenticated()
                )
                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }
}