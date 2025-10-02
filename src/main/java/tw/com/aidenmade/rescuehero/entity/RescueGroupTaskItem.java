package tw.com.aidenmade.rescuehero.entity;

import jakarta.persistence.*;
import lombok.*;
import tw.com.aidenmade.rescuehero.entity.common.AuditInfo;
import tw.com.aidenmade.rescuehero.entity.id.RescueGroupTaskItemMemberRole;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * 救援工項
 */
@Entity
@Table(name = "rescue_group_task_item",
        uniqueConstraints = @UniqueConstraint(columnNames = {"task_id", "name"}),
        indexes = {
                @Index(name = "idx_task_item_audit_id", columnList = "audit_id"),
                @Index(name = "idx_task_item_task_id", columnList = "task_id"),
                @Index(name = "idx_task_item_status_id", columnList = "status_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RescueGroupTaskItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "audit_id", nullable = false)
    private AuditInfo auditInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private RescueGroupTask task;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Builder.Default
    @OneToMany(mappedBy = "taskItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RescueGroupTaskItemMemberRole> memberRoles = new HashSet<>();
}

