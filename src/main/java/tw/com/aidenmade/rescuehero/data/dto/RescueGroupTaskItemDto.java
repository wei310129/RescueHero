package tw.com.aidenmade.rescuehero.data.dto;

import tw.com.aidenmade.rescuehero.data.dto.common.AuditInfoDto;

/**
 * 救援工項
 */
public record RescueGroupTaskItemDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 任務ID
    RescueGroupTaskDto task,
    // 名稱
    String name,
    // 描述
    String description,
    // 狀態ID
    StatusDto status
) {}
