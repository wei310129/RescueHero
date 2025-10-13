package tw.com.aidenmade.rescuehero.data.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tw.com.aidenmade.rescuehero.common.enums.DisasterStatus;
import tw.com.aidenmade.rescuehero.data.projection.DisasterProjection;

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

