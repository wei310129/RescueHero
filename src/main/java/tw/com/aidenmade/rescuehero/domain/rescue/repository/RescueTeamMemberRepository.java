package tw.com.aidenmade.rescuehero.domain.rescue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.domain.rescue.entity.RescueTeamMember;

public interface RescueTeamMemberRepository extends JpaRepository<RescueTeamMember, Long> {
    // ...待會補查詢方法...
}

