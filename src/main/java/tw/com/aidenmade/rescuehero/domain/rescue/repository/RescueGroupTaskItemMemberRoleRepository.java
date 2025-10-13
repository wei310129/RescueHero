package tw.com.aidenmade.rescuehero.domain.rescue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.domain.rescue.entity.RescueGroupTaskItemMemberRole;
import tw.com.aidenmade.rescuehero.domain.rescue.entity.id.RescueGroupTaskItemMemberRoleId;

public interface RescueGroupTaskItemMemberRoleRepository
        extends JpaRepository<RescueGroupTaskItemMemberRole, RescueGroupTaskItemMemberRoleId> {

//    List<RescueGroupTaskItemMemberRole> findByTaskItemId(Long taskItemId);
//
//    List<RescueGroupTaskItemMemberRole> findByMemberId(Long memberId);
}

