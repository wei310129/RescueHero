package tw.com.aidenmade.rescuehero.domain.common.entity;

import jakarta.persistence.*;
import lombok.*;


/**
 * 角色類型表
 */
@Entity
@Table(name = "role_type",
        indexes = @Index(name = "idx_role_type_audit_id", columnList = "audit_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "audit_id", nullable = false)
    private AuditInfo auditInfo;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description", length = 200)
    private String description;
}
