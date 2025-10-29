package tw.com.aidenmade.rescuehero.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class CurrentAuditor implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return Optional.empty();
        }
        Object principal = auth.getPrincipal();
        // 假設 principal 有 getId() 方法
        try {
            var method = principal.getClass().getMethod("getId");
            Object id = method.invoke(principal);
            if (id instanceof Long) {
                return Optional.of((Long) id);
            }
            if (id instanceof Integer) {
                return Optional.of(((Integer) id).longValue());
            }
        } catch (Exception ignored) {}
        return Optional.empty();
    }
}
