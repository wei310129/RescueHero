package tw.com.aidenmade.rescuehero.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.data.entity.Household;

public interface HouseholdRepository extends JpaRepository<Household, Long> {
    // ...待會補查詢方法...
}

