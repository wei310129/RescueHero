package tw.com.aidenmade.rescuehero.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.data.entity.ResourceType;

public interface ResourceTypeRepository extends JpaRepository<ResourceType, Long> {
    // ...待會補查詢方法...
}
