package tw.com.aidenmade.rescuehero.domain.storage.projection;

import tw.com.aidenmade.rescuehero.domain.disaster.application.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.domain.resource.application.dto.ResourceDto;
import tw.com.aidenmade.rescuehero.domain.storage.application.dto.StorageDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.AuditInfoDto;

public interface StorageInventoryProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    DisasterDto getDisaster();
    StorageDto getStorage();
    ResourceDto getResource();
    Integer getQuantity();
}
