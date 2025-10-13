package tw.com.aidenmade.rescuehero.domain.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.domain.storage.entity.Storage;


public interface StorageRepository extends JpaRepository<Storage, Long> {
}

