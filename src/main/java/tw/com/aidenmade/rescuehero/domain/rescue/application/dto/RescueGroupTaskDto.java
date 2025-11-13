package tw.com.aidenmade.rescuehero.domain.rescue.application.dto;

import tw.com.aidenmade.rescuehero.context.TimezoneContext;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.disaster.application.dto.DisasterDto;

import java.time.Instant;
import java.time.ZonedDateTime;

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
    ZonedDateTime assignedAt,
    // 任務完成日期
    ZonedDateTime completedAt,
    // 任務細項
//    List<RescueGroupTaskItemDto> items
    // 新增目前成員人數欄位
    Integer currentMemberCount
) {
    public RescueGroupTaskDto(Long id,
                              AuditInfoDto auditInfo,
//                              RescueGroupDto group,
                              DisasterDto disaster,
                              String name,
                              String description,
                              StatusDto status,
                              Integer priority,
                              Integer minMember,
                              Integer maxMember,
                              Instant assignedAt,
                              Instant completedAt,
                              Integer currentMemberCount
                              ) {
        this(id,
                auditInfo,
                null,
                disaster,
                name,
                description,
                status,
                priority,
                minMember,
                maxMember,
                assignedAt == null ? null : assignedAt.atZone(TimezoneContext.getZone()),
                completedAt == null ? null : completedAt.atZone(TimezoneContext.getZone()),
                currentMemberCount
        );
    }
}
