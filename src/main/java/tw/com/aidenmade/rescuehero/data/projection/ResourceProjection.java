package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;
import tw.com.aidenmade.rescuehero.dto.ResourceTypeDto;

public interface ResourceProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    ResourceTypeDto getResourceType();
    String getName();
    String getUnit();
    String getDescription();
}
