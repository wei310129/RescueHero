package tw.com.aidenmade.rescuehero.data.entity.id;

import jakarta.persistence.*;
import lombok.*;
import tw.com.aidenmade.rescuehero.data.entity.RescueGroupTaskItem;
import tw.com.aidenmade.rescuehero.data.entity.RescueTeamMember;
import tw.com.aidenmade.rescuehero.data.entity.Role;

import java.util.UUID;

@Entity
@Table(name = "rescue_group_task_item_member_role",
        indexes = {
                @Index(name = "idx_task_item_member_role_audit_id", columnList = "audit_id"),
                @Index(name = "idx_task_item_member_role_task_item_id", columnList = "task_item_id"),
                @Index(name = "idx_task_item_member_role_member_id", columnList = "member_id"),
                @Index(name = "idx_task_item_member_role_role_id", columnList = "role_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(RescueGroupTaskItemMemberRoleId.class)
public class RescueGroupTaskItemMemberRole {

    @Column(name = "audit_id", nullable = false)
    private UUID auditId;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_item_id", nullable = false)
    private RescueGroupTaskItem taskItem;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private RescueTeamMember member;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}

