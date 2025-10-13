package tw.com.aidenmade.rescuehero.domain.storage.application.dto;

import tw.com.aidenmade.rescuehero.domain.common.application.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.disaster.application.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.domain.resource.application.dto.ResourceDto;

/**
 * 儲存站物資庫存
 */
public record StorageInventoryDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 災害ID
    DisasterDto disaster,
    // 儲存站ID
    StorageDto storage,
    // 物資ID
    ResourceDto resource,
    // 數量
    Integer quantity
) {}
