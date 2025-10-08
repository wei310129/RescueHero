package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.RescueGroupDto;
import tw.com.aidenmade.rescuehero.data.dto.StatusDto;
import tw.com.aidenmade.rescuehero.data.dto.UnitDto;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

public interface RescueTeamProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    UnitDto getUnit();
    RescueGroupDto getGroup();
    StatusDto getStatus();
}
