package tw.com.aidenmade.rescuehero.domain.address.projection;

import tw.com.aidenmade.rescuehero.domain.common.dto.CountryDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;

public interface AddressLevelProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    CountryDto getCountry();
    String getName();
    String getSuffix();
    Integer getSequence();
}

