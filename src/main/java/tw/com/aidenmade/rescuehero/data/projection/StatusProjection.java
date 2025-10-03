package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;
import tw.com.aidenmade.rescuehero.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.dto.StatusTypeDto;

public interface StatusProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    DisasterDto getDisaster();
    StatusTypeDto getType();
    String getCode();
    String getName();
    String getDescription();
}
