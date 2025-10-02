package tw.com.aidenmade.rescuehero.entity.id;

import lombok.*;

import java.io.Serializable;

/**
 * 複合主鍵類別：RescueGroupTaskItemMemberRole 的複合主鍵
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class RescueGroupTaskItemMemberRoleId implements Serializable {
    private Long taskItem;  // 注意：型別要跟實體裡 @Id 欄位的 PK 型別一致
    private Long member;
    private Long role;
}
