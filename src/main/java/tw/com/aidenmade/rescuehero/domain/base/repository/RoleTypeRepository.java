package tw.com.aidenmade.rescuehero.domain.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.domain.base.entity.RoleType;
import tw.com.aidenmade.rescuehero.domain.base.projection.RoleTypeProjection;

public interface RoleTypeRepository extends JpaRepository<RoleType, Long> {

    RoleTypeProjection findByName(String name);
}
