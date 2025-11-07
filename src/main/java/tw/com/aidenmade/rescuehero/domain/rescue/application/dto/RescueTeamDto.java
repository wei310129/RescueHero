package tw.com.aidenmade.rescuehero.domain.rescue.application.dto;

import tw.com.aidenmade.rescuehero.domain.base.application.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.UnitDto;

import java.util.List;

/**
 * 救援團隊
 */
public record RescueTeamDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 單元ID
    UnitDto unit,
    // 群組ID
    RescueGroupTaskDto groupTask,
    // 狀態ID
    StatusDto status,
    // 最小人數
    Integer minMember,
    // 最大人數
    Integer maxMember,
    // 隊員清單
    List<RescueTeamMemberDto> members
) {}
