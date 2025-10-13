package tw.com.aidenmade.rescuehero.domain.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.domain.common.entity.StatusType;

public interface StatusTypeRepository extends JpaRepository<StatusType, Long> {

}
