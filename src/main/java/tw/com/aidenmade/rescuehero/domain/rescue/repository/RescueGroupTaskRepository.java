package tw.com.aidenmade.rescuehero.domain.rescue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.domain.rescue.entity.RescueGroupTask;

public interface RescueGroupTaskRepository extends JpaRepository<RescueGroupTask, Long> {
    // ...待會補查詢方法...
}

