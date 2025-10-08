package tw.com.aidenmade.rescuehero.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.data.entity.RescueOrganization;

public interface RescueOrganizationRepository extends JpaRepository<RescueOrganization, Long> {
    // ...待會補查詢方法...
}

