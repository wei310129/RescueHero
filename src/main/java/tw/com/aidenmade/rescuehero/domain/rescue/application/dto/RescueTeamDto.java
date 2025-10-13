package tw.com.aidenmade.rescuehero.domain.rescue.application.dto;

import tw.com.aidenmade.rescuehero.domain.common.application.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.UnitDto;

/**
 * 救援團隊
 */
public record RescueTeamDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 單元ID
    UnitDto unit,
    // 群組ID
    RescueGroupDto group,
    // 狀態ID
    StatusDto status
) {}
