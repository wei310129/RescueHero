package tw.com.aidenmade.rescuehero.domain.resource.projection;

import tw.com.aidenmade.rescuehero.domain.base.application.dto.AuditInfoDto;

public interface ResourceTypeProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    String getName();
    String getDescription();
}
