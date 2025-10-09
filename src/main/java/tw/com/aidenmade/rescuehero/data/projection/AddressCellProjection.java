package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.AddressCellDto;
import tw.com.aidenmade.rescuehero.data.dto.AddressLevelDto;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

public interface AddressCellProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    AddressLevelDto getLevel();
    AddressCellDto getParent();
    String getName();
}
