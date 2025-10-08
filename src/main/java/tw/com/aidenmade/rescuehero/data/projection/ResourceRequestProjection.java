package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.data.dto.PersonDto;
import tw.com.aidenmade.rescuehero.data.dto.ResourceDto;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

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
