package tw.com.aidenmade.rescuehero.domain.resource.projection;

import tw.com.aidenmade.rescuehero.domain.disaster.application.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.PersonDto;
import tw.com.aidenmade.rescuehero.domain.resource.application.dto.ResourceDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.AuditInfoDto;

import java.time.ZonedDateTime;

public interface ResourceRequestProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    DisasterDto getDisaster();
    ResourceDto getResource();
    Integer getQuantity();
    PersonDto getRequestedBy();
    ZonedDateTime getRequestedAt();
    Boolean getFulfilled();
    ZonedDateTime getFulfilledAt();
    String getNote();
}
