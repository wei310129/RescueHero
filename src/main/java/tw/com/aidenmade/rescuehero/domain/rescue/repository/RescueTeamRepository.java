package tw.com.aidenmade.rescuehero.domain.rescue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.domain.rescue.entity.RescueTeam;

public interface RescueTeamRepository extends JpaRepository<RescueTeam, Long> {
    // ...待會補查詢方法...
}

