package tw.com.aidenmade.rescuehero.domain.rescue.projection;

import tw.com.aidenmade.rescuehero.domain.disaster.application.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.UnitDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.AuditInfoDto;

public interface RescueOrganizationProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    DisasterDto getDisaster();
    UnitDto getUnit();
    String getDescription();
}
