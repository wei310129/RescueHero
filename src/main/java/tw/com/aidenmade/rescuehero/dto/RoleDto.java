package tw.com.aidenmade.rescuehero.dto;

import lombok.*;

/**
 * 角色
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    // 主鍵
    private Long id;
    // 稽核資訊ID
    private Long auditInfoId;
    // 災害ID
    private Long disasterId;
    // 角色類型ID
    private Long roleTypeId;
    // 名稱
    private String name;
    // 描述
    private String description;
}
