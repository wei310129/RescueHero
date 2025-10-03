package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;
import tw.com.aidenmade.rescuehero.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.dto.StorageDto;
import tw.com.aidenmade.rescuehero.dto.ResourceDto;

public interface StorageInventoryProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    DisasterDto getDisaster();
    StorageDto getStorage();
    ResourceDto getResource();
    Integer getQuantity();
}
