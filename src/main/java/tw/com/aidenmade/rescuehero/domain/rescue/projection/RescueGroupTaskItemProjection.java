package tw.com.aidenmade.rescuehero.domain.rescue.projection;

import tw.com.aidenmade.rescuehero.domain.rescue.dto.RescueGroupTaskDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;

public interface RescueGroupTaskItemProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    RescueGroupTaskDto getTask();
    String getName();
    String getDescription();
    StatusDto getStatus();
}
