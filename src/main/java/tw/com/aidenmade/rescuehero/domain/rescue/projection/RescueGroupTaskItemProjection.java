package tw.com.aidenmade.rescuehero.domain.rescue.projection;

import tw.com.aidenmade.rescuehero.domain.base.projection.AuditInfoProjection;
import tw.com.aidenmade.rescuehero.domain.base.projection.StatusProjection;

public interface RescueGroupTaskItemProjection {
    Long getId();
    AuditInfoProjection getAuditInfo();
    RescueGroupTaskProjection getTask();
    String getName();
    String getDescription();
    StatusProjection getStatus();
}
