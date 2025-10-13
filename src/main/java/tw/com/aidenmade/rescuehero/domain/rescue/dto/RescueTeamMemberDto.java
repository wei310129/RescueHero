package tw.com.aidenmade.rescuehero.domain.rescue.dto;

import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.PersonDto;

/**
 * 救援團隊成員
 */
public record RescueTeamMemberDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 人員ID
    PersonDto person,
    // 團隊ID
    RescueTeamDto team,
    // 組織ID
    RescueOrganizationDto organization
) {}

