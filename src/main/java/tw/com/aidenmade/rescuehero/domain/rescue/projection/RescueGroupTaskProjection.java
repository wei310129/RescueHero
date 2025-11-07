package tw.com.aidenmade.rescuehero.domain.rescue.projection;

import tw.com.aidenmade.rescuehero.domain.base.projection.AuditInfoProjection;
import tw.com.aidenmade.rescuehero.domain.base.projection.StatusProjection;
import tw.com.aidenmade.rescuehero.domain.disaster.projection.DisasterProjection;

import java.time.ZonedDateTime;
import java.util.List;

public interface RescueGroupTaskProjection {
    Long getId();
    AuditInfoProjection getAuditInfo();
    RescueGroupProjection getGroup();
    DisasterProjection getDisaster();
    String getName();
    String getDescription();
    StatusProjection getStatus();
    Integer getPriority();
    Integer getMinMember();
    Integer getMaxMember();
    ZonedDateTime getAssignedAt();
    ZonedDateTime getCompletedAt();
    List<RescueGroupTaskItemProjection> getItems();
}
