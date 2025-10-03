package tw.com.aidenmade.rescuehero.dto;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;
import tw.com.aidenmade.rescuehero.enums.DisasterStatus;

import java.time.ZonedDateTime;

/**
 * 災害
 */
public record DisasterDto(
        // 主鍵 ID
        Long id,
        // 稽核資訊ID
        AuditInfoDto auditInfo,
        // 災害狀態
        DisasterStatus status,
        // 災害名稱
        String name,
        // 發生日期 (只存到日 yyyy-MM-dd)
        ZonedDateTime occurredAt,
        // 發生地點
        String location,
        // 災害描述
        String description
) {}
