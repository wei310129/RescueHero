package tw.com.aidenmade.rescuehero.domain.common.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;


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

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "audit_id", nullable = false)
    private AuditInfo auditInfo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    @Comment("國籍")
    private Country country;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "identification", unique = true, length = 10)
    private String identification;

    @Column(name = "age")
    private Integer age;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "phone", length = 50)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
}
