package tw.com.aidenmade.rescuehero.domain.rescue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.domain.rescue.entity.RescueGroupTaskItem;

public interface RescueGroupTaskItemRepository extends JpaRepository<RescueGroupTaskItem, Long> {
    // ...待會補查詢方法...
}

