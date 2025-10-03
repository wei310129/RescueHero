package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;
import tw.com.aidenmade.rescuehero.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.dto.ResourceDto;
import tw.com.aidenmade.rescuehero.dto.RescueTeamMemberDto;
import tw.com.aidenmade.rescuehero.dto.UnitDto;
import tw.com.aidenmade.rescuehero.dto.PersonDto;

public interface ResourceDistributionProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    DisasterDto getDisaster();
    ResourceDto getResource();
    Integer getQuantity();
    RescueTeamMemberDto getDeliveredBy();
    UnitDto getRecipientUnit();
    PersonDto getRecipientPerson();
}
