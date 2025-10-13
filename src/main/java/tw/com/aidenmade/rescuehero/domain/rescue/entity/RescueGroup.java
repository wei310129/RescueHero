package tw.com.aidenmade.rescuehero.domain.rescue.entity;

import jakarta.persistence.*;
import lombok.*;
import tw.com.aidenmade.rescuehero.domain.common.entity.AuditInfo;
import tw.com.aidenmade.rescuehero.domain.disaster.entity.Disaster;


/**
 * 救援群組
 */
@Entity
@Table(name = "rescue_group",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"organization_id", "disaster_id", "name"}),
                @UniqueConstraint(columnNames = {"id", "disaster_id"})
        },
        indexes = {
                @Index(name = "idx_rescue_group_audit_id", columnList = "audit_id"),
                @Index(name = "idx_rescue_group_disaster_id", columnList = "disaster_id"),
                @Index(name = "idx_rescue_group_org_id", columnList = "organization_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RescueGroup {

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
    @JoinColumn(name = "organization_id", nullable = false)
    private RescueOrganization organization;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
