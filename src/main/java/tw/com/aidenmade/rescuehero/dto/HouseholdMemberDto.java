package tw.com.aidenmade.rescuehero.dto;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;

/**
 * 受災戶成員
 */
public record HouseholdMemberDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 人員ID
    PersonDto person,
    // 受災戶ID
    HouseholdDto household,
    // 狀態ID
    StatusDto status
) {}
