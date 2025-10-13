package tw.com.aidenmade.rescuehero.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tw.com.aidenmade.rescuehero.common.context.TimezoneContext;

import java.io.IOException;
import java.time.ZoneId;

import static tw.com.aidenmade.rescuehero.common.context.TimezoneContext.DEFAULT_ZONE;

@Component
public class TimezoneFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

        String zoneIdHeader = request.getHeader("X-Timezone");
        ZoneId zone = DEFAULT_ZONE;

        if (zoneIdHeader != null && !zoneIdHeader.isBlank()) {
            try {
                zone = ZoneId.of(zoneIdHeader);
            } catch (Exception e) {
                // 傳無效的時區字串 → fallback 預設
                zone = DEFAULT_ZONE;
            }
        }

        // 用 ScopedValue 綁定，讓後續處理鏈都能讀到
        ScopedValue.where(TimezoneContext.CURRENT_ZONE, zone)
                .run(() -> {
                    try {
                        filterChain.doFilter(request, response);
                    } catch (IOException | ServletException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }
}
