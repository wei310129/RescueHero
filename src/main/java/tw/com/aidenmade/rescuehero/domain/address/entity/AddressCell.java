package tw.com.aidenmade.rescuehero.domain.address.entity;

import jakarta.persistence.*;
import lombok.*;
import tw.com.aidenmade.rescuehero.domain.common.entity.AuditInfo;

/**
 * 地址細胞（地點節點）
 */
@Entity
@Table(name = "address_cell",
    uniqueConstraints = @UniqueConstraint(columnNames = {"level_id", "parent_id", "name"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressCell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "audit_id", nullable = false)
    private AuditInfo auditInfo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "level_id", nullable = false)
    private AddressLevel level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private AddressCell parent;

    @Column(name = "name", nullable = false, length = 128)
    private String name;
}

