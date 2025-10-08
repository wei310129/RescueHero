package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.ResourceTypeDto;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

public interface ResourceProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    ResourceTypeDto getResourceType();
    String getName();
    String getUnit();
    String getDescription();
}
