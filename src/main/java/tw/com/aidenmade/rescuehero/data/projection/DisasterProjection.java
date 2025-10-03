package tw.com.aidenmade.rescuehero.data.projection;

import java.time.Instant;
import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;
import tw.com.aidenmade.rescuehero.enums.DisasterStatus;

public interface DisasterProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    DisasterStatus getStatus();
    String getName();
    Instant getOccurredAt();
    String getLocation();
    String getDescription();
}
