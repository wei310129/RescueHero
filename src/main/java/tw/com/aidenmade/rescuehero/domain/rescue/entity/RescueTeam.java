package tw.com.aidenmade.rescuehero.domain.rescue.entity;

import jakarta.persistence.*;
import lombok.*;
import tw.com.aidenmade.rescuehero.domain.common.entity.AuditInfo;
import tw.com.aidenmade.rescuehero.domain.common.entity.Status;
import tw.com.aidenmade.rescuehero.domain.common.entity.Unit;


/**
 * 救援團隊
 */
@Entity
@Table(name = "rescue_team",
        indexes = {
                @Index(name = "idx_rescue_team_audit_id", columnList = "audit_id"),
                @Index(name = "idx_rescue_team_unit_id", columnList = "unit_id"),
                @Index(name = "idx_rescue_team_group_id", columnList = "group_id"),
                @Index(name = "idx_rescue_team_status_id", columnList = "status_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RescueTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "audit_id", nullable = false)
    private AuditInfo auditInfo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false, unique = true)
    private Unit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private RescueGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;
}