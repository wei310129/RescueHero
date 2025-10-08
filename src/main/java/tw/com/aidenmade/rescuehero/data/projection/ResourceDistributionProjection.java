package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.*;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

import java.time.ZonedDateTime;

public interface ResourceDistributionProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    DisasterDto getDisaster();
    ResourceDto getResource();
    Integer getQuantity();
    RescueTeamMemberDto getDeliveredBy();
    UnitDto getRecipientUnit();
    PersonDto getRecipientPerson();
    ZonedDateTime getDeliveredAt();
    String getNote();
}
