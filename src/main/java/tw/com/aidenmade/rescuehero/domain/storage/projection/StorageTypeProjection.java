package tw.com.aidenmade.rescuehero.domain.storage.projection;

import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;

public interface StorageTypeProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    String getName();
    String getDescription();
}
