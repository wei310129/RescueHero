package tw.com.aidenmade.rescuehero.domain.household.entity;

import jakarta.persistence.*;
import lombok.*;
import tw.com.aidenmade.rescuehero.domain.common.entity.AuditInfo;
import tw.com.aidenmade.rescuehero.domain.common.entity.Person;
import tw.com.aidenmade.rescuehero.domain.common.entity.Status;


/**
 * 受災戶成員
 */
@Entity
@Table(name = "household_member",
        indexes = {
                @Index(name = "idx_household_member_audit_id", columnList = "audit_id"),
                @Index(name = "idx_household_member_person_id", columnList = "person_id"),
                @Index(name = "idx_household_member_household_id", columnList = "household_id"),
                @Index(name = "idx_household_member_status_id", columnList = "status_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HouseholdMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "audit_id", nullable = false)
    private AuditInfo auditInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "household_id", nullable = false)
    private Household household;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;
}
