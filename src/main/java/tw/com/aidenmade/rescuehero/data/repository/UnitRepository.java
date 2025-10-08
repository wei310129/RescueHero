package tw.com.aidenmade.rescuehero.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tw.com.aidenmade.rescuehero.data.entity.Unit;
import tw.com.aidenmade.rescuehero.data.projection.UnitProjection;

import java.util.List;
import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, Long> {
}

