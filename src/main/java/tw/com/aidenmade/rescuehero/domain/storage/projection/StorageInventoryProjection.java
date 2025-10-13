package tw.com.aidenmade.rescuehero.domain.storage.projection;

import tw.com.aidenmade.rescuehero.domain.disaster.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.domain.resource.dto.ResourceDto;
import tw.com.aidenmade.rescuehero.domain.storage.dto.StorageDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;

public interface StorageInventoryProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    DisasterDto getDisaster();
    StorageDto getStorage();
    ResourceDto getResource();
    Integer getQuantity();
}
