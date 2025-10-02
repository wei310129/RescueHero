package tw.com.aidenmade.rescuehero.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * 人員表
 */
@Entity
@Table(name = "person",
        indexes = @Index(name = "idx_person_audit_id", columnList = "audit_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "audit_id", nullable = false)
    private UUID auditId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "identification", unique = true, length = 10)
    private String identification;

    @Column(name = "phone", length = 50)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;
}
