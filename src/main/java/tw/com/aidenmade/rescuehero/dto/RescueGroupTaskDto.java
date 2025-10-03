package tw.com.aidenmade.rescuehero.dto;

import tw.com.aidenmade.rescuehero.dto.common.AuditInfoDto;

/**
 * 群組救援任務
 */
public record RescueGroupTaskDto(
    // 主鍵
    Long id,
    // 稽核資訊ID
    AuditInfoDto auditInfo,
    // 群組ID
    RescueGroupDto rescueGroup,
    // 災害ID
    DisasterDto disaster,
    // 名稱
    String name,
    // 描述
    String description
) {}
