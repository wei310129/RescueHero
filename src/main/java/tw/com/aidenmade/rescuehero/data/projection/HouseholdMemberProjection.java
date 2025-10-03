package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;
import tw.com.aidenmade.rescuehero.dto.PersonDto;
import tw.com.aidenmade.rescuehero.dto.HouseholdDto;
import tw.com.aidenmade.rescuehero.dto.StatusDto;

public interface HouseholdMemberProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    PersonDto getPerson();
    HouseholdDto getHousehold();
    StatusDto getStatus();
}
