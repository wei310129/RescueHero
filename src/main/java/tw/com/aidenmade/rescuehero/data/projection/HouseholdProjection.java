package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.data.dto.StatusDto;
import tw.com.aidenmade.rescuehero.data.dto.UnitDto;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

public interface HouseholdProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    UnitDto getUnit();
    DisasterDto getDisaster();
    StatusDto getStatus();
    String getNote();
}
