package tw.com.aidenmade.rescuehero.domain.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.domain.base.entity.Unit;


public interface UnitRepository extends JpaRepository<Unit, Long> {
}

