package tw.com.aidenmade.rescuehero.domain.resource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.domain.resource.entity.ResourceRequest;

public interface ResourceRequestRepository extends JpaRepository<ResourceRequest, Long> {
    // ...待會補查詢方法...
}
