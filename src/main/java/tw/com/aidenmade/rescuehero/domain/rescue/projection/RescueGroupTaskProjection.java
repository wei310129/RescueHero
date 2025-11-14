package tw.com.aidenmade.rescuehero.domain.rescue.projection;

import tw.com.aidenmade.rescuehero.domain.base.projection.AuditInfoProjection;
import tw.com.aidenmade.rescuehero.domain.base.projection.StatusProjection;
import tw.com.aidenmade.rescuehero.domain.disaster.projection.DisasterProjection;

import java.time.Instant;

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
    Instant getAssignedAt();
    Instant getCompletedAt();
    Integer getCurrentMemberCount();
//    有需要再查詢，避免查詢效能低落，以及增加程式碼的維護難度
//    List<RescueGroupTaskItemProjection> getItems();
}
