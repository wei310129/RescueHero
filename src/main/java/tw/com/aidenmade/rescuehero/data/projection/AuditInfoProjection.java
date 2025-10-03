package tw.com.aidenmade.rescuehero.data.projection;

import java.time.Instant;
import java.util.UUID;

public interface AuditInfoProjection {
    UUID getId();
    Instant getCreatedAt();
    Instant getUpdatedAt();
    String getCreatedBy();
}

