package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.data.dto.PersonDto;
import tw.com.aidenmade.rescuehero.data.dto.ResourceDto;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

public interface ResourceRequestProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    DisasterDto getDisaster();
    ResourceDto getResource();
    Integer getQuantity();
    PersonDto getRequestedBy();
}
