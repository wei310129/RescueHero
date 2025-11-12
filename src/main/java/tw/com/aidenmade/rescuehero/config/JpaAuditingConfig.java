package tw.com.aidenmade.rescuehero.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "currentAuditor")
public class JpaAuditingConfig {
}
