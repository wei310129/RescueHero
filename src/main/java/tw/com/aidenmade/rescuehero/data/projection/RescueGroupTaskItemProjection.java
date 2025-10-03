package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;
import tw.com.aidenmade.rescuehero.dto.RescueGroupTaskDto;
import tw.com.aidenmade.rescuehero.dto.StatusDto;

public interface RescueGroupTaskItemProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    RescueGroupTaskDto getTask();
    String getName();
    String getDescription();
    StatusDto getStatus();
}
