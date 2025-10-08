package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.PersonDto;
import tw.com.aidenmade.rescuehero.data.dto.RescueOrganizationDto;
import tw.com.aidenmade.rescuehero.data.dto.RescueTeamDto;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

public interface RescueTeamMemberProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    PersonDto getPerson();
    RescueTeamDto getTeam();
    RescueOrganizationDto getOrganization();
}
