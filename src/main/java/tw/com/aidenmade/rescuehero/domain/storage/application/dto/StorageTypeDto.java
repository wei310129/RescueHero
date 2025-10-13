package tw.com.aidenmade.rescuehero.domain.storage.application.dto;

import tw.com.aidenmade.rescuehero.domain.common.application.dto.AuditInfoDto;

/**
 * 儲存站類型
 */
public record StorageTypeDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 名稱
    String name,
    // 描述
    String description
) {}
