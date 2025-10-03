package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;
import tw.com.aidenmade.rescuehero.dto.UnitDto;
import tw.com.aidenmade.rescuehero.dto.RescueGroupDto;
import tw.com.aidenmade.rescuehero.dto.StatusDto;

public interface RescueTeamProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    UnitDto getUnit();
    RescueGroupDto getGroup();
    StatusDto getStatus();
}
