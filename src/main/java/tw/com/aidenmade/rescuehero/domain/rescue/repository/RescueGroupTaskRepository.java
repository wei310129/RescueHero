package tw.com.aidenmade.rescuehero.domain.rescue.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import tw.com.aidenmade.rescuehero.domain.rescue.entity.QRescueGroupTask;
import tw.com.aidenmade.rescuehero.domain.rescue.entity.RescueGroupTask;
import tw.com.aidenmade.rescuehero.domain.rescue.projection.RescueGroupTaskProjection;

import java.util.List;
import java.util.Optional;

public interface RescueGroupTaskRepository
        extends JpaRepository<RescueGroupTask, Long>, RescueGroupTaskQueryDslRepository {
    // ...existing code...
}

interface RescueGroupTaskQueryDslRepository {
    Page<RescueGroupTaskProjection> findPageByConditions(Long groupId, Long disasterId, String nameLike, Long statusId, Integer priority, Pageable pageable);
}

class RescueGroupTaskQueryDslRepositoryImpl extends QuerydslRepositorySupport implements RescueGroupTaskQueryDslRepository {
    private final JPAQueryFactory queryFactory;

    public RescueGroupTaskQueryDslRepositoryImpl(EntityManager entityManager) {
        super(RescueGroupTask.class);
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<RescueGroupTaskProjection> findPageByConditions(Long groupId, Long disasterId, String nameLike, Long statusId, Integer priority, Pageable pageable) {
        QRescueGroupTask task = QRescueGroupTask.rescueGroupTask;
        BooleanExpression predicate = task.isNotNull();
        if (groupId != null) predicate = predicate.and(task.group.id.eq(groupId));
        if (disasterId != null) predicate = predicate.and(task.disaster.id.eq(disasterId));
        if (nameLike != null && !nameLike.isEmpty()) predicate = predicate.and(task.name.containsIgnoreCase(nameLike));
        if (statusId != null) predicate = predicate.and(task.status.id.eq(statusId));
        if (priority != null) predicate = predicate.and(task.priority.eq(priority));
        List<RescueGroupTaskProjection> content = queryFactory
            .select(Projections.bean(RescueGroupTaskProjection.class,
                task.id,
                task.auditInfo,
                task.group,
                task.disaster,
                task.name,
                task.description,
                task.status,
                task.priority,
                task.minMember,
                task.maxMember,
                task.assignedAt,
                task.completedAt
            ))
            .from(task)
            .where(predicate)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
        Long total = Optional
                .ofNullable(
                        queryFactory.select(task.count()).from(task).where(predicate).fetchOne())
                .orElse(0L);
        return new PageImpl<>(content, pageable, total);
    }
}
