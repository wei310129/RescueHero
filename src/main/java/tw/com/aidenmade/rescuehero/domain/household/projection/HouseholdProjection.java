package tw.com.aidenmade.rescuehero.domain.household.projection;

import tw.com.aidenmade.rescuehero.domain.disaster.application.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.UnitDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.AuditInfoDto;

public interface HouseholdProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    UnitDto getUnit();
    DisasterDto getDisaster();
    StatusDto getStatus();
    String getNote();
}
