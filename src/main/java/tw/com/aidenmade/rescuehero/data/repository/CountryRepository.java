package tw.com.aidenmade.rescuehero.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.data.entity.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
    // ...待會補查詢方法...
}

