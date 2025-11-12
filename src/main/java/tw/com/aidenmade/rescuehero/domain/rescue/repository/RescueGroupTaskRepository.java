package tw.com.aidenmade.rescuehero.domain.rescue.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import tw.com.aidenmade.rescuehero.domain.account.entity.QAccount;
import tw.com.aidenmade.rescuehero.domain.address.application.dto.AddressDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.CountryDto;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.StatusDto;
import tw.com.aidenmade.rescuehero.domain.base.entity.QAuditInfo;
import tw.com.aidenmade.rescuehero.domain.base.entity.QStatus;
import tw.com.aidenmade.rescuehero.domain.disaster.application.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.domain.disaster.entity.QDisaster;
import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueGroupTaskDto;
import tw.com.aidenmade.rescuehero.domain.rescue.entity.QRescueGroup;
import tw.com.aidenmade.rescuehero.domain.rescue.entity.QRescueGroupTask;
import tw.com.aidenmade.rescuehero.domain.rescue.entity.QRescueOrganization;
import tw.com.aidenmade.rescuehero.domain.rescue.entity.RescueGroupTask;

import java.util.List;
import java.util.Optional;

public interface RescueGroupTaskRepository
        extends JpaRepository<RescueGroupTask, Long>, RescueGroupTaskQueryDslRepository {
    // ...existing code...
}

interface RescueGroupTaskQueryDslRepository {
    Page<RescueGroupTaskDto> findPageByConditions(Long groupId, Long disasterId, String nameLike, Long statusId, Integer priority, Pageable pageable);
}

class RescueGroupTaskQueryDslRepositoryImpl extends QuerydslRepositorySupport implements RescueGroupTaskQueryDslRepository {
    private final JPAQueryFactory queryFactory;

    public RescueGroupTaskQueryDslRepositoryImpl(EntityManager entityManager) {
        super(RescueGroupTask.class);
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<RescueGroupTaskDto> findPageByConditions(
            Long groupId, Long disasterId, String nameLike, Long statusId, Integer priority, Pageable pageable) {

        QRescueGroupTask task = QRescueGroupTask.rescueGroupTask;
        QRescueGroup group = QRescueGroup.rescueGroup;
        QRescueOrganization org = QRescueOrganization.rescueOrganization;
        QDisaster disaster = QDisaster.disaster;
        QStatus status = QStatus.status;
        QAuditInfo audit = QAuditInfo.auditInfo;
        QAccount createdBy = QAccount.account;
        QAccount updatedBy = QAccount.account;

        BooleanExpression predicate = task.isNotNull();
        if (groupId != null) predicate = predicate.and(task.group.id.eq(groupId));
        if (disasterId != null) predicate = predicate.and(task.disaster.id.eq(disasterId));
        if (nameLike != null && !nameLike.isEmpty()) predicate = predicate.and(task.name.containsIgnoreCase(nameLike));
        if (statusId != null) predicate = predicate.and(task.status.id.eq(statusId));
        if (priority != null) predicate = predicate.and(task.priority.eq(priority));

        // 排序
        List<OrderSpecifier<?>> orders = List.of(task.id.desc());

        // count 查詢
        long total = Optional.ofNullable(
                queryFactory.select(task.count()).from(task).where(predicate).fetchOne()
        ).orElse(0L);
        if (total == 0) return new PageImpl<>(List.of(), pageable, 0);

        // 主查詢
        List<RescueGroupTaskDto> content = queryFactory
                .select(Projections.constructor(RescueGroupTaskDto.class,
                        task.id,
//                    Projections.constructor(AuditInfoDto.class,
//                            audit.id,
//                            audit.createdAt,
//                            audit.updatedAt,
//                            Projections.constructor(AccountDto.class, createdBy),
//                            Projections.constructor(AccountDto.class, updatedBy.id)
//                    ),
//                    task.auditInfo,
//                    Projections.constructor(RescueGroupDto.class,
//                            group.id,
//                            Projections.constructor(RescueOrganizationDto.class,
//                                    org.id, org.description
//                            ),
//                            group.name, group.description
//                    ),
                        Projections.constructor(DisasterDto.class,
                                disaster.id,
                                Projections.constructor(CountryDto.class,
                                        disaster.country.id,
                                        disaster.country.name,
                                        disaster.country.nativeName,
                                        disaster.country.code
                                ),
                                disaster.status,
                                disaster.name,
                                disaster.occurredAt,
                                Projections.constructor(AddressDto.class,
                                        disaster.location.fullAddress
                                ),
                                disaster.description
                        ),
                        task.name,
                        task.description,
                        Projections.constructor(StatusDto.class,
                                status.id,
//                            status.type,
                                status.code, status.name, status.description
                        ),
                        task.priority,
                        task.minMember,
                        task.maxMember,
                        task.assignedAt,
                        task.completedAt
                ))
                .from(task)
//            .leftJoin(task.group, group)
//            .leftJoin(group.organization, org)
            .leftJoin(task.disaster, disaster)
            .leftJoin(task.status, status)
//            .leftJoin(task.auditInfo, audit)
//            .leftJoin(audit.createdBy, createdBy)
//            .leftJoin(audit.updatedBy, updatedBy)
                .where(predicate)
                .orderBy(orders.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }
}
