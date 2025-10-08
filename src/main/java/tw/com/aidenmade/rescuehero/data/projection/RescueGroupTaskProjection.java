package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.data.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.data.dto.RescueGroupDto;
import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

public interface RescueGroupTaskProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    RescueGroupDto getGroup();
    DisasterDto getDisaster();
    String getName();
    String getDescription();
}
