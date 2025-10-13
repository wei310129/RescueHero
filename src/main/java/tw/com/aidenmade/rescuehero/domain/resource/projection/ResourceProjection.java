package tw.com.aidenmade.rescuehero.domain.resource.projection;

import tw.com.aidenmade.rescuehero.domain.resource.dto.ResourceTypeDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;

public interface ResourceProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    ResourceTypeDto getResourceType();
    String getName();
    String getUnit();
    String getDescription();
}
