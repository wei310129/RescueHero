package tw.com.aidenmade.rescuehero.domain.resource.projection;

import tw.com.aidenmade.rescuehero.domain.resource.application.dto.ResourceTypeDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.AuditInfoDto;

public interface ResourceProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    ResourceTypeDto getResourceType();
    String getName();
    String getUnit();
    String getDescription();
}
