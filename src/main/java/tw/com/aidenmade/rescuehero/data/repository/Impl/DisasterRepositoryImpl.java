package tw.com.aidenmade.rescuehero.data.repository.Impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import tw.com.aidenmade.rescuehero.data.entity.QDisaster;
import tw.com.aidenmade.rescuehero.data.projection.DisasterProjection;
import tw.com.aidenmade.rescuehero.data.repository.custom.DisasterRepositoryCustom;
import tw.com.aidenmade.rescuehero.enums.DisasterStatus;

import java.time.Instant;
import java.util.List;

@Repository
public class DisasterRepositoryImpl implements DisasterRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public DisasterRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<DisasterProjection> searchDisasters(String name, Long countryId, DisasterStatus status, Instant occurredStart, Instant occurredEnd, Pageable pageable) {
        QDisaster disaster = QDisaster.disaster;
        BooleanExpression predicate = disaster.isNotNull();
        if (name != null && !name.isEmpty()) {
            predicate = predicate.and(disaster.name.containsIgnoreCase(name));
        }
        if (countryId != null) {
            predicate = predicate.and(disaster.country.id.eq(countryId));
        }
        if (status != null) {
            predicate = predicate.and(disaster.status.eq(status));
        }
        if (occurredStart != null && occurredEnd != null) {
            predicate = predicate.and(disaster.occurredAt.between(occurredStart, occurredEnd));
        } else if (occurredStart != null) {
            predicate = predicate.and(disaster.occurredAt.goe(occurredStart));
        } else if (occurredEnd != null) {
            predicate = predicate.and(disaster.occurredAt.loe(occurredEnd));
        }
        List<DisasterProjection> content = queryFactory
            .select(Projections.bean(DisasterProjection.class,
                disaster.id,
                disaster.name,
                disaster.country,
                disaster.status,
                disaster.occurredAt
            ))
            .from(disaster)
            .where(predicate)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
        Long total = queryFactory
            .select(disaster.count())
            .from(disaster)
            .where(predicate)
            .fetchOne();
        total = (total == null) ? 0 : total;
        return new PageImpl<>(content, pageable, total);
    }
}
