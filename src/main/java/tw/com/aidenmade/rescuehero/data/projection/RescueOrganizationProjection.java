package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.dto.UnitDto;
import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;

public interface RescueOrganizationProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    DisasterDto getDisaster();
    UnitDto getUnit();
    String getDescription();
}
