package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.HouseholdDto;
import tw.com.aidenmade.rescuehero.data.dto.PersonDto;
import tw.com.aidenmade.rescuehero.data.dto.StatusDto;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

public interface HouseholdMemberProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    PersonDto getPerson();
    HouseholdDto getHousehold();
    StatusDto getStatus();
}
