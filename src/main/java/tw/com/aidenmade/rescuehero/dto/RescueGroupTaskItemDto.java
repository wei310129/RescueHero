package tw.com.aidenmade.rescuehero.dto;

import lombok.*;

/**
 * 救援工項
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RescueGroupTaskItemDto {
    // 主鍵
    private Long id;
    // 稽核資訊ID
    private Long auditInfoId;
    // 任務ID
    private Long taskId;
    // 名稱
    private String name;
    // 描述
    private String description;
    // 狀態ID
    private Long statusId;
}
