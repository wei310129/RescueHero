package tw.com.aidenmade.rescuehero.domain.common.projection;

import tw.com.aidenmade.rescuehero.domain.address.dto.AddressDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.CountryDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;

public interface UnitProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    CountryDto getCountry();
    String getName();
    AddressDto getAddress();
    String getContactName();
    String getContactPhone();
}
