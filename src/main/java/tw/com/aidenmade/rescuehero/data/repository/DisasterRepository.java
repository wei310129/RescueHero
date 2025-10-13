package tw.com.aidenmade.rescuehero.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.common.enums.DisasterStatus;
import tw.com.aidenmade.rescuehero.data.entity.Disaster;
import tw.com.aidenmade.rescuehero.data.projection.DisasterProjection;
import tw.com.aidenmade.rescuehero.data.repository.custom.DisasterRepositoryCustom;

import java.time.Instant;
import java.util.Optional;

public interface DisasterRepository extends JpaRepository<Disaster, Long>, DisasterRepositoryCustom {

    // 依ID查詢 projection
    Optional<DisasterProjection> findProjectedById(Long id);

    // 查詢所有（直接轉 projection，並支援分頁）
    Page<DisasterProjection> findAllBy(Pageable pageable);

    // 自訂查詢方法
    Page<DisasterProjection> searchDisasters(
        String name,
        Long countryId,
        DisasterStatus status,
        Instant occurredStart,
        Instant occurredEnd,
        Pageable pageable
    );
}
