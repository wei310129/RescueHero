package tw.com.aidenmade.rescuehero.domain.household.dto;

import tw.com.aidenmade.rescuehero.domain.common.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.disaster.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.common.dto.UnitDto;

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
