package tw.com.aidenmade.rescuehero.domain.address.projection;

import tw.com.aidenmade.rescuehero.domain.base.projection.AuditInfoProjection;
import tw.com.aidenmade.rescuehero.domain.base.projection.CountryProjection;

public interface AddressProjection {
    Long getId();
    AuditInfoProjection getAuditInfo();
    CountryProjection getCountry();
    AddressCellProjection getAddressCell();
    String getDetail();
    String getFullAddress();
    Double getLatitude();
    Double getLongitude();
}

