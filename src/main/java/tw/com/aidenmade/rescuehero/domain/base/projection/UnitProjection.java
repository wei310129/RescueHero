package tw.com.aidenmade.rescuehero.domain.base.projection;

import tw.com.aidenmade.rescuehero.domain.address.projection.AddressProjection;

public interface UnitProjection {
    Long getId();
    AuditInfoProjection getAuditInfo();
    CountryProjection getCountry();
    String getName();
    AddressProjection getAddress();
    String getContactName();
    String getContactPhone();
}
