package tw.com.aidenmade.rescuehero.domain.disaster.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tw.com.aidenmade.rescuehero.domain.disaster.enums.DisasterStatus;
import tw.com.aidenmade.rescuehero.domain.disaster.projection.DisasterProjection;

import java.time.Instant;

public interface DisasterRepositoryCustom {
    Page<DisasterProjection> searchDisasters(
        String name,
        Long countryId,
        DisasterStatus status,
        Instant occurredStart,
        Instant occurredEnd,
        Pageable pageable
    );
}

