package tw.com.aidenmade.rescuehero.entity;

import jakarta.persistence.*;
import lombok.*;
import tw.com.aidenmade.rescuehero.entity.common.AuditInfo;


/**
 * 受災戶
 */
@Entity
@Table(name = "household",
        indexes = {
                @Index(name = "idx_household_audit_id", columnList = "audit_id"),
                @Index(name = "idx_household_unit_id", columnList = "unit_id"),
                @Index(name = "idx_household_disaster_id", columnList = "disaster_id"),
                @Index(name = "idx_household_status_id", columnList = "status_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Household {

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
    @JoinColumn(name = "disaster_id")
    private Disaster disaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
}
