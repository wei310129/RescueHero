package tw.com.aidenmade.rescuehero.dto;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;

import java.time.ZonedDateTime;

/**
 * 物資分配紀錄
 */
public record ResourceDistributionDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 災害ID
    DisasterDto disaster,
    // 物資ID
    ResourceDto resource,
    // 數量
    Integer quantity,
    // 配送人員ID
    RescueTeamMemberDto deliveredBy,
    // 受領單位ID
    UnitDto recipientUnit,
    // 受領人員ID
    PersonDto recipientPerson,
    // 配送時間
    ZonedDateTime deliveredAt,
    // 備註
    String note
) {}

