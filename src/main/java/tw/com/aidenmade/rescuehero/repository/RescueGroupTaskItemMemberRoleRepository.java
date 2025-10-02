package tw.com.aidenmade.rescuehero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.entity.id.RescueGroupTaskItemMemberRole;
import tw.com.aidenmade.rescuehero.entity.id.RescueGroupTaskItemMemberRoleId;

public interface RescueGroupTaskItemMemberRoleRepository
        extends JpaRepository<RescueGroupTaskItemMemberRole, RescueGroupTaskItemMemberRoleId> {

//    List<RescueGroupTaskItemMemberRole> findByTaskItemId(Long taskItemId);
//
//    List<RescueGroupTaskItemMemberRole> findByMemberId(Long memberId);
}

