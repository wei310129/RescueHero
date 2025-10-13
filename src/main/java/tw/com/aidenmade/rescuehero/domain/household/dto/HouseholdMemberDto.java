package tw.com.aidenmade.rescuehero.domain.household.dto;

import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.PersonDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.StatusDto;

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
