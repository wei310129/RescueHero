package tw.com.aidenmade.rescuehero.domain.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.domain.base.entity.Role;
import tw.com.aidenmade.rescuehero.domain.base.projection.RoleProjection;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    List<RoleProjection> findByRoleTypeId(Long roleTypeId);
}
