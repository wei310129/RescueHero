package tw.com.aidenmade.rescuehero.domain.rescue.projection;

import tw.com.aidenmade.rescuehero.domain.common.dto.PersonDto;
import tw.com.aidenmade.rescuehero.domain.rescue.dto.RescueOrganizationDto;
import tw.com.aidenmade.rescuehero.domain.rescue.dto.RescueTeamDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;

public interface RescueTeamMemberProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    PersonDto getPerson();
    RescueTeamDto getTeam();
    RescueOrganizationDto getOrganization();
}
