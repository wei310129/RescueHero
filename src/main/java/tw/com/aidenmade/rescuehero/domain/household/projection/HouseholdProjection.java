package tw.com.aidenmade.rescuehero.domain.household.projection;

import tw.com.aidenmade.rescuehero.domain.disaster.application.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.UnitDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.AuditInfoDto;

public interface HouseholdProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    UnitDto getUnit();
    DisasterDto getDisaster();
    StatusDto getStatus();
    String getNote();
}
