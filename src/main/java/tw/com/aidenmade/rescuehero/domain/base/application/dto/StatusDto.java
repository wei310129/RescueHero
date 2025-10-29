package tw.com.aidenmade.rescuehero.domain.base.application.dto;

import tw.com.aidenmade.rescuehero.domain.disaster.application.dto.DisasterDto;

/**
 * 狀態
 */
public record StatusDto(
    // 主鍵 ID
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 所屬災害ID
    DisasterDto disaster,
    // 狀態類型ID
    StatusTypeDto type,
    // 狀態代碼 (ex: pending, completed)
    String code,
    // 顯示名稱 (例: 待處理, 已完成)
    String name,
    // 狀態說明
    String description
) {}
