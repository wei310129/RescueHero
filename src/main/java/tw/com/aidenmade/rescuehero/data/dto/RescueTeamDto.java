package tw.com.aidenmade.rescuehero.data.dto;

import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

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
