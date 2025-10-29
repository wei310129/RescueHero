package tw.com.aidenmade.rescuehero.domain.address.entity;

import jakarta.persistence.*;
import lombok.*;
import tw.com.aidenmade.rescuehero.domain.base.entity.AuditInfo;
import tw.com.aidenmade.rescuehero.domain.base.entity.Country;

/**
 * 地址單位層級（如縣市、區、鄉、路、街）
 */
@Entity
@Table(name = "address_level",
    uniqueConstraints = @UniqueConstraint(columnNames = {"country_id", "name", "sequence"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "audit_id", nullable = false)
    private AuditInfo auditInfo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(name = "name", nullable = false, length = 32)
    private String name; // 層級名稱

    @Column(name = "suffix", length = 32)
    private String suffix; // 後綴字

    @Column(name = "sequence", nullable = false)
    private Integer sequence; // 層級順序
}

