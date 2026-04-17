package tw.com.aidenmade.rescuehero.domain.account.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.aidenmade.rescuehero.domain.account.entity.Account;
import tw.com.aidenmade.rescuehero.domain.account.projection.AccountProjection;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // 依ID查詢 projection
    Optional<AccountProjection> findProjectedByUsernameAndPasswordHash(String username, String passwordHash);

    Optional<AccountProjection> findProjectedById(Long id);

    Optional<AccountProjection> findProjectedByUsername(String username);

    Optional<AccountProjection> findProjectedByEmail(String email);


    // 查詢所有（直接轉 projection，並支援分頁）
    Page<AccountProjection> findAllBy(Pageable pageable);

    // Keyset 分批查詢：id > lastId，每批上限 100 筆，避免 OFFSET 效能問題
    List<AccountProjection> findTop100ByIdGreaterThanOrderByIdAsc(Long lastId);

}
