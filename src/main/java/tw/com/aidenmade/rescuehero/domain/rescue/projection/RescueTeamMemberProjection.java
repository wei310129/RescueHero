package tw.com.aidenmade.rescuehero.domain.rescue.projection;

import tw.com.aidenmade.rescuehero.domain.base.projection.AuditInfoProjection;
import tw.com.aidenmade.rescuehero.domain.base.projection.PersonProjection;

public interface RescueTeamMemberProjection {
    Long getId();
    AuditInfoProjection getAuditInfo();
    PersonProjection getPerson();
    RescueTeamProjection getTeam();
    RescueOrganizationProjection getOrganization();
}
