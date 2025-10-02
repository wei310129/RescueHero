package tw.com.aidenmade.rescuehero.entity;

import jakarta.persistence.*;
import lombok.*;
import tw.com.aidenmade.rescuehero.entity.id.RescueGroupTaskItemMemberRole;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * 救援團隊成員
 */
@Entity
@Table(name = "rescue_team_member",
        uniqueConstraints = @UniqueConstraint(columnNames = {"person_id", "team_id"}),
        indexes = {
                @Index(name = "idx_rescue_team_member_audit_id", columnList = "audit_id"),
                @Index(name = "idx_rescue_team_member_person_id", columnList = "person_id"),
                @Index(name = "idx_rescue_team_member_team_id", columnList = "team_id"),
                @Index(name = "idx_rescue_team_member_org_id", columnList = "organization_id"),
                @Index(name = "idx_rescue_team_member_status_id", columnList = "status_id"),
                @Index(name = "idx_rescue_team_member_role_id", columnList = "role_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RescueTeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "audit_id", nullable = false)
    private UUID auditId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private RescueTeam team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private RescueOrganization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RescueGroupTaskItemMemberRole> taskItemRoles = new HashSet<>();
}