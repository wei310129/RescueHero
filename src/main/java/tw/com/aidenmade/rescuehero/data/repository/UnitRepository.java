package tw.com.aidenmade.rescuehero.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.data.entity.Unit;


public interface UnitRepository extends JpaRepository<Unit, Long> {
}

