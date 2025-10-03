package tw.com.aidenmade.rescuehero.data.entity;

import jakarta.persistence.*;
import lombok.*;
import tw.com.aidenmade.rescuehero.data.entity.common.AuditInfo;
import tw.com.aidenmade.rescuehero.data.entity.id.RescueGroupTaskItemMemberRole;

import java.util.HashSet;
import java.util.Set;

/**
 * 角色表
 */
@Entity
@Table(name = "role",
        uniqueConstraints = @UniqueConstraint(columnNames = {"disaster_id", "type_id", "name"}),
        indexes = {
                @Index(name = "idx_role_audit_id", columnList = "audit_id"),
                @Index(name = "idx_role_disaster_id", columnList = "disaster_id"),
                @Index(name = "idx_role_type_id", columnList = "type_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "audit_id", nullable = false)
    private AuditInfo auditInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disaster_id", nullable = false)
    private Disaster disaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private RoleType roleType;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RescueGroupTaskItemMemberRole> taskItemMembers = new HashSet<>();
}
