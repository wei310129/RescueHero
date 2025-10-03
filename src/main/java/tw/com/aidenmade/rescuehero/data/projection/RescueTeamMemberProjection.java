package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;
import tw.com.aidenmade.rescuehero.dto.PersonDto;
import tw.com.aidenmade.rescuehero.dto.RescueTeamDto;
import tw.com.aidenmade.rescuehero.dto.RescueOrganizationDto;

public interface RescueTeamMemberProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    PersonDto getPerson();
    RescueTeamDto getTeam();
    RescueOrganizationDto getOrganization();
}
