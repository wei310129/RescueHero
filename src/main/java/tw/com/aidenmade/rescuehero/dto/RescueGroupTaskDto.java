package tw.com.aidenmade.rescuehero.dto;

import lombok.*;

/**
 * 群組救援任務
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RescueGroupTaskDto {
    // 主鍵
    private Long id;
    // 稽核資訊ID
    private Long auditInfoId;
    // 群組ID
    private Long groupId;
    // 災害ID
    private Long disasterId;
    // 名稱
    private String name;
    // 描述
    private String description;
}

