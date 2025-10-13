package tw.com.aidenmade.rescuehero.domain.rescue.projection;

import tw.com.aidenmade.rescuehero.domain.common.application.dto.PersonDto;
import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueOrganizationDto;
import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueTeamDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.AuditInfoDto;

public interface RescueTeamMemberProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    PersonDto getPerson();
    RescueTeamDto getTeam();
    RescueOrganizationDto getOrganization();
}
