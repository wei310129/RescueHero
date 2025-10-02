package tw.com.aidenmade.rescuehero.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

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

    @Column(name = "audit_id", nullable = false)
    private UUID auditId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private StorageType storageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "latitude", precision = 9, scale = 6)
    private java.math.BigDecimal latitude;

    @Column(name = "longitude", precision = 9, scale = 6)
    private java.math.BigDecimal longitude;

    @Column(name = "contact_name", length = 100)
    private String contactName;

    @Column(name = "contact_phone", length = 50)
    private String contactPhone;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;
}
