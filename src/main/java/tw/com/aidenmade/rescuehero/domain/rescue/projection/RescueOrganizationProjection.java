package tw.com.aidenmade.rescuehero.domain.rescue.projection;

import tw.com.aidenmade.rescuehero.domain.base.projection.AuditInfoProjection;
import tw.com.aidenmade.rescuehero.domain.base.projection.UnitProjection;
import tw.com.aidenmade.rescuehero.domain.disaster.projection.DisasterProjection;

public interface RescueOrganizationProjection {
    Long getId();
    AuditInfoProjection getAuditInfo();
    DisasterProjection getDisaster();
    UnitProjection getUnit();
    String getDescription();
}
