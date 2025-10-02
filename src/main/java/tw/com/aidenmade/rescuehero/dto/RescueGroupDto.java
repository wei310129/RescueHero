package tw.com.aidenmade.rescuehero.dto;

import lombok.*;

/**
 * 救援群組
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RescueGroupDto {
    // 主鍵
    private Long id;
    // 稽核資訊ID
    private Long auditInfoId;
    // 災害ID
    private Long disasterId;
    // 組織ID
    private Long organizationId;
    // 名稱
    private String name;
    // 描述
    private String description;
}
