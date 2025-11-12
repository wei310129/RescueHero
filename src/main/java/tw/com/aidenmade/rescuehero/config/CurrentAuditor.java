package tw.com.aidenmade.rescuehero.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import tw.com.aidenmade.rescuehero.context.AuditScopes;
import tw.com.aidenmade.rescuehero.domain.account.entity.Account;
import tw.com.aidenmade.rescuehero.principal.UserPrincipal;

import java.util.Optional;

@Component("currentAuditor")
public class CurrentAuditor implements AuditorAware<Account> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Account> getCurrentAuditor() {
        // 檢查是否暫停審計
        if (AuditScopes.AUDIT_BYPASS.isBound() && Boolean.TRUE.equals(AuditScopes.AUDIT_BYPASS.get())) {
            return Optional.empty();
        }

        // 檢查是否覆寫審計帳號
        if (AuditScopes.AUDITOR_ID_OVERRIDE.isBound()) {
            Long id = AuditScopes.AUDITOR_ID_OVERRIDE.get();
            return Optional.of(em.getReference(Account.class, id));
        }

        // 一般情況：取目前登入者
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return Optional.empty();
        Object principal = auth.getPrincipal();
        if (principal instanceof UserPrincipal up && up.getId() != null) {
            return Optional.of(em.getReference(Account.class, up.getId()));
        }
        return Optional.empty();
    }
}
