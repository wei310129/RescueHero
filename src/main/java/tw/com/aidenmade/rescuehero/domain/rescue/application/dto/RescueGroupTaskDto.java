package tw.com.aidenmade.rescuehero.domain.rescue.application.dto;

import tw.com.aidenmade.rescuehero.domain.base.application.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.disaster.application.dto.DisasterDto;

import java.time.Instant;

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
    String description,
    // 狀態
    StatusDto status,
    // 優先度
    Integer priority,
    // 最少成員
    Integer minMember,
    // 最多成員
    Integer maxMember,
    // 任務設立日期
    Instant assignedAt,
    // 任務完成日期
    Instant completedAt
    // 任務細項
//    List<RescueGroupTaskItemDto> items
) {
    public RescueGroupTaskDto(Long id,
//                              AuditInfoDto auditInfo,
//                              RescueGroupDto group,
                              DisasterDto disaster,
                              String name,
                              String description,
                              StatusDto status,
                              Integer priority,
                              Integer minMember,
                              Integer maxMember,
                              Instant assignedAt,
                              Instant completedAt) {
        this(id, null, null, disaster, name, description, status, priority, minMember, maxMember, assignedAt, completedAt);
    }
}
