package tw.com.aidenmade.rescuehero.dto;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;

/**
 * 狀態類型
 */
public record StatusTypeDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 名稱
    String name,
    // 描述
    String description
) {}
