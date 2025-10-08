package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.dto.AddressLevelDto;
import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;

public interface AddressCellProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    AddressLevelDto getLevel();
    AddressCellProjection getParent();
    String getName();
}

