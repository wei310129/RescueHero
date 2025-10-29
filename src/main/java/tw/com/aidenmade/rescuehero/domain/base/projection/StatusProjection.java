package tw.com.aidenmade.rescuehero.domain.base.projection;

import tw.com.aidenmade.rescuehero.domain.disaster.projection.DisasterProjection;

public interface StatusProjection {
    Long getId();
    AuditInfoProjection getAuditInfo();
    DisasterProjection getDisaster();
    StatusTypeProjection getType();
    String getCode();
    String getName();
    String getDescription();
}
