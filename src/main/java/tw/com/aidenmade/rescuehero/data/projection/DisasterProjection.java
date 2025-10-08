package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.AddressDto;
import tw.com.aidenmade.rescuehero.data.dto.CountryDto;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;
import tw.com.aidenmade.rescuehero.enums.DisasterStatus;

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
