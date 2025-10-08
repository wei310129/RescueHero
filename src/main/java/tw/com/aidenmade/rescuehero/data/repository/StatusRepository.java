package tw.com.aidenmade.rescuehero.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.data.entity.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {

}
