package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.RescueGroupTaskDto;
import tw.com.aidenmade.rescuehero.data.dto.StatusDto;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

public interface RescueGroupTaskItemProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    RescueGroupTaskDto getTask();
    String getName();
    String getDescription();
    StatusDto getStatus();
}
