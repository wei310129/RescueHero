package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.common.enums.DisasterStatus;
import tw.com.aidenmade.rescuehero.data.dto.AddressDto;
import tw.com.aidenmade.rescuehero.data.dto.CountryDto;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

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
