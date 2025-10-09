package tw.com.aidenmade.rescuehero.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.data.entity.AddressLevel;
import tw.com.aidenmade.rescuehero.data.projection.AddressLevelProjection;

import java.util.Optional;

public interface AddressLevelRepository extends JpaRepository<AddressLevel, Long> {
    // 依ID查詢 projection
    Optional<AddressLevelProjection> findProjectionById(Long id);

    // 依 country 查詢分頁
    Page<AddressLevelProjection> findByCountryId(Long countryId, Pageable pageable);
}
