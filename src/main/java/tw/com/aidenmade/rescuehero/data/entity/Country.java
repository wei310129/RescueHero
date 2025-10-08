package tw.com.aidenmade.rescuehero.data.entity;

import jakarta.persistence.*;
import lombok.*;
import tw.com.aidenmade.rescuehero.data.entity.common.AuditInfo;

/**
 * 國家
 */
@Entity
@Table(name = "country")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "audit_id", nullable = false)
    private AuditInfo auditInfo;

    @Column(name = "name", nullable = false, unique = true, length = 64)
    private String name; // 國家名稱

    @Column(name = "native_name", length = 128)
    private String nativeName; // 本地語言名稱

    @Column(name = "code", nullable = false, unique = true, length = 8)
    private String code; // 國家代碼 (全大寫，如 TW, US)
}
