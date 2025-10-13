package tw.com.aidenmade.rescuehero.domain.household.projection;

import tw.com.aidenmade.rescuehero.domain.household.dto.HouseholdDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.PersonDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;

public interface HouseholdMemberProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    PersonDto getPerson();
    HouseholdDto getHousehold();
    StatusDto getStatus();
}
