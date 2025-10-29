package tw.com.aidenmade.rescuehero.domain.rescue.projection;

import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueGroupTaskDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.AuditInfoDto;

public interface RescueGroupTaskItemProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    RescueGroupTaskDto getTask();
    String getName();
    String getDescription();
    StatusDto getStatus();
}
