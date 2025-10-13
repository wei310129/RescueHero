package tw.com.aidenmade.rescuehero.domain.household.application.dto;

import tw.com.aidenmade.rescuehero.domain.common.application.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.disaster.application.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.UnitDto;

/**
 * 受災戶
 */
public record HouseholdDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 單元ID
    UnitDto unit,
    // 災害ID
    DisasterDto disaster,
    // 狀態ID
    StatusDto status,
    // 備註
    String note
) {}
