package tw.com.aidenmade.rescuehero.domain.resource.dto;

import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;

/**
 * 物資類型
 */
public record ResourceTypeDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 名稱
    String name,
    // 描述
    String description
) {}
