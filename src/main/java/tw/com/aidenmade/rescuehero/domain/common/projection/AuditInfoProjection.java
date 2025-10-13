package tw.com.aidenmade.rescuehero.domain.common.projection;

import java.time.Instant;
import java.util.UUID;

public interface AuditInfoProjection {
    UUID getId();
    Instant getCreatedAt();
    Instant getUpdatedAt();
    String getCreatedBy();
}

