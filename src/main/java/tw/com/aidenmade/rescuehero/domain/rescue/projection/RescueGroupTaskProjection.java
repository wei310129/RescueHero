package tw.com.aidenmade.rescuehero.domain.rescue.projection;

import tw.com.aidenmade.rescuehero.domain.base.projection.AuditInfoProjection;
import tw.com.aidenmade.rescuehero.domain.disaster.projection.DisasterProjection;

import java.util.List;

public interface RescueGroupTaskProjection {
    Long getId();
    AuditInfoProjection getAuditInfo();
    RescueGroupProjection getGroup();
    DisasterProjection getDisaster();
    String getName();
    String getDescription();
    Integer getMinMember();
    Integer getMaxMember();
    List<RescueGroupTaskItemProjection> getItems();
}
