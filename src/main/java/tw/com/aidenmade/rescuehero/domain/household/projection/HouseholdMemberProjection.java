package tw.com.aidenmade.rescuehero.domain.household.projection;

import tw.com.aidenmade.rescuehero.domain.household.application.dto.HouseholdDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.PersonDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.AuditInfoDto;

public interface HouseholdMemberProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    PersonDto getPerson();
    HouseholdDto getHousehold();
    StatusDto getStatus();
}
