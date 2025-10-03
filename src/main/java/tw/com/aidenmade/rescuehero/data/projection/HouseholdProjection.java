package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;
import tw.com.aidenmade.rescuehero.dto.UnitDto;
import tw.com.aidenmade.rescuehero.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.dto.StatusDto;

public interface HouseholdProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    UnitDto getUnit();
    DisasterDto getDisaster();
    StatusDto getStatus();
    String getNote();
}
