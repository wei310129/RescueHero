package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.data.dto.StatusTypeDto;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

public interface StatusProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    DisasterDto getDisaster();
    StatusTypeDto getType();
    String getCode();
    String getName();
    String getDescription();
}
