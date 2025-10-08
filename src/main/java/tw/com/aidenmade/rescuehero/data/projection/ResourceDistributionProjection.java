package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.*;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

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
