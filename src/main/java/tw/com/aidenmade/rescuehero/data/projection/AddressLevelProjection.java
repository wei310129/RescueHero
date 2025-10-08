package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.CountryDto;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

public interface AddressLevelProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    CountryDto getCountry();
    String getName();
    String getSuffix();
    Integer getSequence();
}

