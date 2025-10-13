package tw.com.aidenmade.rescuehero.domain.household.projection;

import tw.com.aidenmade.rescuehero.domain.disaster.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.UnitDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;

public interface HouseholdProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    UnitDto getUnit();
    DisasterDto getDisaster();
    StatusDto getStatus();
    String getNote();
}
