package tw.com.aidenmade.rescuehero.dto;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;

/**
 * 物資
 */
public record ResourceDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 物資類型ID
    ResourceTypeDto resourceType,
    // 名稱
    String name,
    // 計數單位
    String unit,
    // 描述
    String description
) {}

