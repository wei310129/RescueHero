package tw.com.aidenmade.rescuehero.domain.address.projection;

import tw.com.aidenmade.rescuehero.domain.address.application.dto.AddressCellDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.CountryDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.AuditInfoDto;

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

