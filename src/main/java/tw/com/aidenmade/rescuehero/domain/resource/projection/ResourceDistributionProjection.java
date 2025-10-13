package tw.com.aidenmade.rescuehero.domain.resource.projection;

import tw.com.aidenmade.rescuehero.domain.common.application.dto.PersonDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.UnitDto;
import tw.com.aidenmade.rescuehero.domain.disaster.application.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueTeamMemberDto;
import tw.com.aidenmade.rescuehero.domain.resource.application.dto.ResourceDto;

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
