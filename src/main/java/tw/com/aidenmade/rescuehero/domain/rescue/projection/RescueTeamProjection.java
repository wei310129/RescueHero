package tw.com.aidenmade.rescuehero.domain.rescue.projection;

import tw.com.aidenmade.rescuehero.domain.base.projection.AuditInfoProjection;
import tw.com.aidenmade.rescuehero.domain.base.projection.StatusProjection;
import tw.com.aidenmade.rescuehero.domain.base.projection.UnitProjection;

import java.util.List;

public interface RescueTeamProjection {
    Long getId();
    AuditInfoProjection getAuditInfo();
    UnitProjection getUnit();
    RescueGroupTaskProjection getGroupTask();
    StatusProjection getStatus();
    Integer getMinMember();
    Integer getMaxMember();
    List<RescueTeamMemberProjection> getMembers();
}
