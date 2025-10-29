package tw.com.aidenmade.rescuehero.domain.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.domain.base.entity.AuditInfo;

import java.util.UUID;

public interface AuditInfoRepository extends JpaRepository<AuditInfo, UUID> {
}
