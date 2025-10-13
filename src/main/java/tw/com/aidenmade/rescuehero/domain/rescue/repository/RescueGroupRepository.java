package tw.com.aidenmade.rescuehero.domain.rescue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.domain.rescue.entity.RescueGroup;

public interface RescueGroupRepository extends JpaRepository<RescueGroup, Long> {
    // ...待會補查詢方法...
}

