package tw.com.aidenmade.rescuehero.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.data.entity.StorageType;

public interface StorageTypeRepository extends JpaRepository<StorageType, Long> {

}
