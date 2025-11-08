package tw.com.aidenmade.rescuehero.domain.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.domain.base.entity.Status;
import tw.com.aidenmade.rescuehero.domain.base.projection.StatusProjection;

import java.util.List;

public interface StatusRepository extends JpaRepository<Status, Long> {
    List<StatusProjection> findByType_Name(String name);
}
