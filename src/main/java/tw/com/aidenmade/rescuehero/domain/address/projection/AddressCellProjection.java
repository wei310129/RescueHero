package tw.com.aidenmade.rescuehero.domain.address.projection;

import tw.com.aidenmade.rescuehero.domain.base.projection.AuditInfoProjection;

public interface AddressCellProjection {
    Long getId();
    AuditInfoProjection getAuditInfo();
    AddressLevelProjection getLevel();
    AddressCellProjection getParent();
    String getName();
}
