package tw.com.aidenmade.rescuehero.domain.storage.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import tw.com.aidenmade.rescuehero.domain.address.entity.Address;
import tw.com.aidenmade.rescuehero.domain.common.entity.AuditInfo;
import tw.com.aidenmade.rescuehero.domain.common.entity.Country;
import tw.com.aidenmade.rescuehero.domain.common.entity.Status;


/**
 * 災害物資庫存站
 */
@Entity
@Table(name = "storage",
        uniqueConstraints = @UniqueConstraint(columnNames = {"type_id", "name"}),
        indexes = {
                @Index(name = "idx_storage_audit_id", columnList = "audit_id"),
                @Index(name = "idx_storage_type_id", columnList = "type_id"),
                @Index(name = "idx_storage_status_id", columnList = "status_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "audit_id", nullable = false)
    private AuditInfo auditInfo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    @Comment("國籍")
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private StorageType storageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location", nullable = false)
    @Comment("倉庫地址")
    private Address location;

    @Column(name = "contact_name", length = 100)
    @Comment("聯絡人姓名")
    private String contactName;

    @Column(name = "contact_phone", length = 50)
    @Comment("聯絡人電話")
    private String contactPhone;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;
}
