package tw.com.aidenmade.rescuehero.domain.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.domain.base.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
    // ...待會補查詢方法...
}
