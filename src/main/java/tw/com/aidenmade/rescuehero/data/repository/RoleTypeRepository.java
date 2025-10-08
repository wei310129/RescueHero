package tw.com.aidenmade.rescuehero.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.data.entity.RoleType;

public interface RoleTypeRepository extends JpaRepository<RoleType, Long> {
}
