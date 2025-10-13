package tw.com.aidenmade.rescuehero.domain.rescue.application.dto;

import tw.com.aidenmade.rescuehero.domain.common.application.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.disaster.application.dto.DisasterDto;

/**
 * 群組救援任務
 */
public record RescueGroupTaskDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 群組ID
    RescueGroupDto group,
    // 災害ID
    DisasterDto disaster,
    // 名稱
    String name,
    // 描述
    String description
) {}
