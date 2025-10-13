package tw.com.aidenmade.rescuehero.domain.address.projection;

import tw.com.aidenmade.rescuehero.domain.address.dto.AddressCellDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.CountryDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;

public interface AddressProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    CountryDto getCountry();
    AddressCellDto getAddressCell();
    String getDetail();
    String getFullAddress();
    Double getLatitude();
    Double getLongitude();
}

