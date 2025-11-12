package tw.com.aidenmade.rescuehero.domain.rescue.projection;

import tw.com.aidenmade.rescuehero.domain.base.projection.AuditInfoProjection;
import tw.com.aidenmade.rescuehero.domain.disaster.projection.DisasterProjection;

public interface RescueGroupProjection {
    Long getId();
    AuditInfoProjection getAuditInfo();
    DisasterProjection getDisaster();
    RescueOrganizationProjection getOrganization();
    String getName();
    String getDescription();
}
