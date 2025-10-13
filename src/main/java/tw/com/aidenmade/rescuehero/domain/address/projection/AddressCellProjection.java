package tw.com.aidenmade.rescuehero.domain.address.projection;

import tw.com.aidenmade.rescuehero.domain.address.application.dto.AddressCellDto;
import tw.com.aidenmade.rescuehero.domain.address.application.dto.AddressLevelDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.AuditInfoDto;

public interface AddressCellProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    AddressLevelDto getLevel();
    AddressCellDto getParent();
    String getName();
}
