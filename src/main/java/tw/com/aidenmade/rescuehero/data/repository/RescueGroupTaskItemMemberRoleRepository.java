package tw.com.aidenmade.rescuehero.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.data.entity.id.RescueGroupTaskItemMemberRole;
import tw.com.aidenmade.rescuehero.data.entity.id.RescueGroupTaskItemMemberRoleId;

public interface RescueGroupTaskItemMemberRoleRepository
        extends JpaRepository<RescueGroupTaskItemMemberRole, RescueGroupTaskItemMemberRoleId> {

//    List<RescueGroupTaskItemMemberRole> findByTaskItemId(Long taskItemId);
//
//    List<RescueGroupTaskItemMemberRole> findByMemberId(Long memberId);
}

