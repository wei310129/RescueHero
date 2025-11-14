package tw.com.aidenmade.rescuehero.domain.address.projection;

import tw.com.aidenmade.rescuehero.domain.base.projection.AuditInfoProjection;
import tw.com.aidenmade.rescuehero.domain.base.projection.CountryProjection;

public interface AddressLevelProjection {
    Long getId();
    AuditInfoProjection getAuditInfo();
    CountryProjection getCountry();
    String getName();
    String getSuffix();
    Integer getSequence();
}

