package tw.com.aidenmade.rescuehero.domain.rescue.projection;

import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueGroupDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.UnitDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.AuditInfoDto;

public interface RescueTeamProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    UnitDto getUnit();
    RescueGroupDto getGroup();
    StatusDto getStatus();
}
