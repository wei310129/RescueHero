package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.dto.AddressCellDto;
import tw.com.aidenmade.rescuehero.dto.CountryDto;
import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;

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

