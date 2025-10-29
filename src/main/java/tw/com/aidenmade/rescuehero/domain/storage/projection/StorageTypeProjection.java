package tw.com.aidenmade.rescuehero.domain.storage.projection;

import tw.com.aidenmade.rescuehero.domain.base.application.dto.AuditInfoDto;

public interface StorageTypeProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    String getName();
    String getDescription();
}
