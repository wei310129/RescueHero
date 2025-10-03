package tw.com.aidenmade.rescuehero.data.entity;

import jakarta.persistence.*;
import lombok.*;
import tw.com.aidenmade.rescuehero.data.entity.common.AuditInfo;

import java.time.Instant;

/**
 * 群組救援任務
 */
@Entity
@Table(name = "rescue_group_task",
        uniqueConstraints = @UniqueConstraint(columnNames = {"group_id", "name"}),
        indexes = {
                @Index(name = "idx_group_task_audit_id", columnList = "audit_id"),
                @Index(name = "idx_group_task_group_id", columnList = "group_id"),
                @Index(name = "idx_group_task_disaster_id", columnList = "disaster_id"),
                @Index(name = "idx_group_task_status_id", columnList = "status_id"),
                @Index(name = "idx_group_task_priority", columnList = "priority")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RescueGroupTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "audit_id", nullable = false)
    private AuditInfo auditInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private RescueGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disaster_id", nullable = false)
    private Disaster disaster;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "priority", nullable = false)
    private Integer priority; // 1=最高, 5=最低

    @Column(name = "assigned_at")
    private Instant assignedAt;

    @Column(name = "completed_at")
    private Instant completedAt;
}
