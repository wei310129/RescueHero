package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;
import tw.com.aidenmade.rescuehero.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.dto.ResourceDto;
import tw.com.aidenmade.rescuehero.dto.PersonDto;

public interface ResourceRequestProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    DisasterDto getDisaster();
    ResourceDto getResource();
    Integer getQuantity();
    PersonDto getRequestedBy();
}
