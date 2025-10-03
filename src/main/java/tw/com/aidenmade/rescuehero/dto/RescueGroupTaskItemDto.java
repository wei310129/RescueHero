package tw.com.aidenmade.rescuehero.dto;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;

/**
 * 救援工項
 */
public record RescueGroupTaskItemDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 任務ID
    RescueGroupTaskDto rescueGroupTask,
    // 名稱
    String name,
    // 描述
    String description,
    // 狀態ID
    Long statusId
) {}
