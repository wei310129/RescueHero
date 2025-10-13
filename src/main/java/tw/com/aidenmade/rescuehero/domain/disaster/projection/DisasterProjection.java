package tw.com.aidenmade.rescuehero.domain.disaster.projection;

import tw.com.aidenmade.rescuehero.domain.address.dto.AddressDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.CountryDto;
import tw.com.aidenmade.rescuehero.domain.disaster.enums.DisasterStatus;

import java.time.Instant;

public interface DisasterProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    CountryDto getCountry();
    DisasterStatus getStatus();
    String getName();
    Instant getOccurredAt();
    AddressDto getLocation();
    String getDescription();
}
