package tw.com.aidenmade.rescuehero.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.data.entity.AddressCell;
import tw.com.aidenmade.rescuehero.data.projection.AddressCellProjection;

import java.util.Optional;

public interface AddressCellRepository extends JpaRepository<AddressCell, Long> {
    // 查詢 id 為 parentId 的 AddressCell projection
    Optional<AddressCellProjection> findProjectionById(Long id);
}
