package tw.com.aidenmade.rescuehero.domain.resource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.domain.resource.entity.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    // ...existing code...
}
