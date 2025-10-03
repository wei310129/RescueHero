package tw.com.aidenmade.rescuehero.data.projection;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;
import tw.com.aidenmade.rescuehero.dto.RescueGroupDto;
import tw.com.aidenmade.rescuehero.dto.DisasterDto;

public interface RescueGroupTaskProjection {
    Long getId();
    AuditInfoDto getAuditInfo();
    RescueGroupDto getGroup();
    DisasterDto getDisaster();
    String getName();
    String getDescription();
}
