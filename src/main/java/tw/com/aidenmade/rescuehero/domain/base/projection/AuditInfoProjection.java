package tw.com.aidenmade.rescuehero.domain.base.projection;

import tw.com.aidenmade.rescuehero.domain.account.projection.AccountProjection;

import java.time.Instant;
import java.util.UUID;

public interface AuditInfoProjection {
    UUID getId();
    Instant getCreatedAt();
    Instant getUpdatedAt();
    AccountProjection getCreatedBy();
    AccountProjection getUpdatedBy();
}

