package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.AddressDto;
import tw.com.aidenmade.rescuehero.data.dto.CountryDto;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

public interface UnitProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    CountryDto getCountry();
    String getName();
    AddressDto getAddress();
    String getContactName();
    String getContactPhone();
}
