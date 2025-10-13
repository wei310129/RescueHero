package tw.com.aidenmade.rescuehero.domain.common.application.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * 審計資訊基礎類別
 * 提供共用的審計欄位：id, created_at, updated_at, created_by
 */
public record AuditInfoDto(
    // 主鍵
    UUID id,
    // 建立時間
    ZonedDateTime createdAt,
    // 更新時間
    ZonedDateTime updatedAt,
    // 建立者
    String createdBy
) {}

