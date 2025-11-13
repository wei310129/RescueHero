package tw.com.aidenmade.rescuehero.domain.base.application.dto;

import tw.com.aidenmade.rescuehero.domain.base.mapstruct.utils.TimeMapper;

import java.time.Instant;
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
    Long createdBy,
    // 修改者
    Long updatedBy
) {
    public AuditInfoDto(
//            UUID id,
            Instant createdAt,
            Instant updatedAt,
            Long createdBy,
            Long updatedBy
    ) {
        this(
                null,
                TimeMapper.INSTANCE.toZonedDateTime(createdAt),
                TimeMapper.INSTANCE.toZonedDateTime(updatedAt),
                createdBy,
                updatedBy
        );
    }
}

