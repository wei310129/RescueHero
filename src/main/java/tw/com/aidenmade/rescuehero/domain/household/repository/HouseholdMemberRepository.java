package tw.com.aidenmade.rescuehero.domain.household.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.domain.household.entity.HouseholdMember;

public interface HouseholdMemberRepository extends JpaRepository<HouseholdMember, Long> {
    // ...待會補查詢方法...
}

