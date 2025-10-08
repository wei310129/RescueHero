package tw.com.aidenmade.rescuehero.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.data.entity.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    // ...existing code...
}
