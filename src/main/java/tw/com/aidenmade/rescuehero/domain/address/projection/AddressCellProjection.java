package tw.com.aidenmade.rescuehero.domain.address.projection;

import tw.com.aidenmade.rescuehero.domain.address.dto.AddressCellDto;
import tw.com.aidenmade.rescuehero.domain.address.dto.AddressLevelDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;

public interface AddressCellProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    AddressLevelDto getLevel();
    AddressCellDto getParent();
    String getName();
}
