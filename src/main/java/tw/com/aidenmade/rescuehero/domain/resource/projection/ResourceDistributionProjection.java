package tw.com.aidenmade.rescuehero.domain.resource.projection;

import tw.com.aidenmade.rescuehero.domain.common.dto.PersonDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.UnitDto;
import tw.com.aidenmade.rescuehero.domain.disaster.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.rescue.dto.RescueTeamMemberDto;
import tw.com.aidenmade.rescuehero.domain.resource.dto.ResourceDto;

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
