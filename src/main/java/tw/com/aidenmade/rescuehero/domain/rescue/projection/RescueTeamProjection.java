package tw.com.aidenmade.rescuehero.domain.rescue.projection;

import tw.com.aidenmade.rescuehero.domain.rescue.dto.RescueGroupDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.UnitDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;

public interface RescueTeamProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    UnitDto getUnit();
    RescueGroupDto getGroup();
    StatusDto getStatus();
}
