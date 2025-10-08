package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.data.dto.ResourceDto;
import tw.com.aidenmade.rescuehero.data.dto.StorageDto;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

public interface StorageInventoryProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    DisasterDto getDisaster();
    StorageDto getStorage();
    ResourceDto getResource();
    Integer getQuantity();
}
