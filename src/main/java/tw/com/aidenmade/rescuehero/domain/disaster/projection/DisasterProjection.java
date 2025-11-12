package tw.com.aidenmade.rescuehero.domain.disaster.projection;

import tw.com.aidenmade.rescuehero.domain.address.projection.AddressProjection;
import tw.com.aidenmade.rescuehero.domain.base.projection.AuditInfoProjection;
import tw.com.aidenmade.rescuehero.domain.base.projection.CountryProjection;
import tw.com.aidenmade.rescuehero.domain.disaster.enums.DisasterStatus;

import java.time.Instant;

public interface DisasterProjection {
    Long getId();
    AuditInfoProjection getAuditInfo();
    CountryProjection getCountry();
    DisasterStatus getStatus();
    String getName();
    Instant getOccurredAt();
    AddressProjection getLocation();
    String getDescription();
}
