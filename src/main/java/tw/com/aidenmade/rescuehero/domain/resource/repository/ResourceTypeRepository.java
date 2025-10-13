package tw.com.aidenmade.rescuehero.domain.resource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.domain.resource.entity.ResourceType;

public interface ResourceTypeRepository extends JpaRepository<ResourceType, Long> {
    // ...待會補查詢方法...
}
