package tw.com.aidenmade.rescuehero.domain.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.domain.common.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
    // ...待會補查詢方法...
}
