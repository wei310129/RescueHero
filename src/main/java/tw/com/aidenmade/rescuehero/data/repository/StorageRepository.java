package tw.com.aidenmade.rescuehero.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tw.com.aidenmade.rescuehero.data.entity.Storage;
import tw.com.aidenmade.rescuehero.data.projection.StorageProjection;

import java.util.List;
import java.util.Optional;

public interface StorageRepository extends JpaRepository<Storage, Long> {
}

