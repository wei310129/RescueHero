package tw.com.aidenmade.rescuehero.data.projection;

import java.math.BigDecimal;
import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;
import tw.com.aidenmade.rescuehero.dto.StorageTypeDto;
import tw.com.aidenmade.rescuehero.dto.StatusDto;

public interface StorageProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    StorageTypeDto getStorageType();
    StatusDto getStatus();
    String getName();
    String getAddress();
    BigDecimal getLatitude();
    BigDecimal getLongitude();
}
