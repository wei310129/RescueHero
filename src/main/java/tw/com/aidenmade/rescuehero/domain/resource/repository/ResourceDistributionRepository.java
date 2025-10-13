package tw.com.aidenmade.rescuehero.domain.resource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.domain.resource.entity.ResourceDistribution;

public interface ResourceDistributionRepository extends JpaRepository<ResourceDistribution, Long> {
    // ...待會補查詢方法...
}
