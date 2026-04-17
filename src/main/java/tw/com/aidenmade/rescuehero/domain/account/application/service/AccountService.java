package tw.com.aidenmade.rescuehero.domain.account.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tw.com.aidenmade.rescuehero.configuration.context.AuditScopesContext;
import tw.com.aidenmade.rescuehero.domain.account.api.request.*;
import tw.com.aidenmade.rescuehero.domain.account.application.dto.AccountDto;
import tw.com.aidenmade.rescuehero.domain.account.entity.Account;
import tw.com.aidenmade.rescuehero.domain.account.mapstruct.AccountMapper;
import tw.com.aidenmade.rescuehero.domain.account.mapstruct.AccountProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.account.repository.AccountRepository;
import tw.com.aidenmade.rescuehero.domain.base.entity.AuditInfo;
import tw.com.aidenmade.rescuehero.domain.base.entity.Role;
import tw.com.aidenmade.rescuehero.domain.base.repository.AuditInfoRepository;
import tw.com.aidenmade.rescuehero.utils.ExceptionUtils;
import tw.com.aidenmade.rescuehero.utils.PasswordUtils;

import java.time.Instant;
import java.util.List;
import tw.com.aidenmade.rescuehero.domain.account.projection.AccountProjection;

@Slf4j
@Service
public class AccountService extends AbstractAccountBaseService {
    private final AccountMapper accountMapper;
    private final AuditInfoRepository auditInfoRepository;

    public AccountService(
            AccountRepository accountRepository,
            AccountProjectionMapper accountProjectionMapper,
            AccountMapper accountMapper,
            AuditInfoRepository auditInfoRepository
    ) {
        super(accountRepository, accountProjectionMapper);
        this.accountMapper = accountMapper;
        this.auditInfoRepository = auditInfoRepository;
    }

    public Page<AccountDto> listAll(Pageable pageable) {
        return accountRepository.findAllBy(pageable)
                .map(accountProjectionMapper::toDto);
    }

    /**
     * 非阻塞查詢單一帳號 — 回傳 Mono<AccountDto>
     * 將阻塞的 JPA 查詢卸載至 boundedElastic 執行緒池，避免阻塞事件迴圈；
     * 查無帳號時回傳 Mono.empty()
     */
    public Mono<AccountDto> getByIdMono(Long id) {
        return Mono.fromCallable(() ->
                accountRepository.findProjectedById(id)
                        .map(accountProjectionMapper::toDto)
                        .orElse(null)
        ).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * 非阻塞查詢所有帳號 — 回傳 Flux<AccountDto>（逐筆串流，Keyset 分批）
     * 在 boundedElastic 執行緒池上執行迴圈，每批最多 100 筆；
     * 以 id > lastId ORDER BY id 取代 OFFSET，避免深頁效能退化。
     */
    public Flux<AccountDto> listAllFlux() {
        return Flux.<AccountDto>create(sink -> {
            long lastId = 0L;
            while (!sink.isCancelled()) {
                List<AccountProjection> batch =
                        accountRepository.findTop100ByIdGreaterThanOrderByIdAsc(lastId);
                if (batch.isEmpty()) break;
                batch.forEach(p -> sink.next(accountProjectionMapper.toDto(p)));
                lastId = batch.getLast().getId();
                if (batch.size() < 100) break; // 最後一批
            }
            sink.complete();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public boolean register(AccountCreateRequest request) {
        try {
            return AuditScopesContext.runWithoutAuditing(() -> {
                Instant now = Instant.now();

                // 1) 第一次 insert audit_info：不寫 createdBy/updatedBy（避免 FK）
                AuditInfo auditInfo = auditInfoRepository.save(
                        AuditInfo.builder()
                                .createdAt(now)
                                .updatedAt(now)
                                // 不要手動塞 createdBy/updatedBy
                                .build()
                );

                // 2) 建立 account（掛上剛產生的 audit）
                Account account = accountRepository.save(
                        Account.builder()
                                .auditInfo(auditInfo)
                                .role(Role.builder().id(request.getRoleId()).build())
                                .username(request.getUsername())
                                .passwordHash(PasswordUtils.encryptPassword(request.getPassword()))
                                .email(request.getEmail())
                                .build()
                );

                // 3) 回填 audit 的 createdBy/updatedBy 為「新帳號本人」
                auditInfo.setCreatedBy(account);
                auditInfo.setUpdatedBy(account);
                auditInfoRepository.save(auditInfo);

                return true;
            });
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("註冊 Account 失敗", e);
            return false;
        }
    }

    private void updateAuditInfo(Account account) {
        AuditInfo auditInfo = account.getAuditInfo();
        if (auditInfo != null) {
            auditInfo.setUpdatedAt(Instant.now());
            auditInfo.setUpdatedBy(account); // 若只存 id，改為 account.getId()
            auditInfoRepository.save(auditInfo);
        }
    }

    @Transactional
    public boolean updateInsensitive(AccountUpdateInsensitiveRequest request) {
        try {
            Account account = accountRepository.findById(request.getId())
                    .orElseThrow(() -> ExceptionUtils.cannotFindId(request.getId()));
            accountMapper.updateInsensitive(account, request);
            updateAuditInfo(account);
            accountRepository.save(account);
            return true;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("更新 Account 失敗", e);
            return false;
        }
    }

    @Transactional
    public boolean updatePassword(AccountUpdatePasswordRequest request) {
        try {
            Account account = accountRepository.findById(request.getId())
                    .orElseThrow(() -> ExceptionUtils.cannotFindId(request.getId()));
            account.setPasswordHash(PasswordUtils.encryptPassword(request.getPassword()));
            updateAuditInfo(account);
            accountRepository.save(account);
            return true;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("更新 Account 密碼失敗", e);
            return false;
        }
    }

    @Transactional
    public boolean updateIsAdmin(AccountUpdateAdminRequest request) {
        try {
            Account account = accountRepository.findById(request.getId())
                    .orElseThrow(() -> ExceptionUtils.cannotFindId(request.getId()));
            account.setIsAdmin(request.getIsAdmin());
            updateAuditInfo(account);
            accountRepository.save(account);
            return true;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("更新 Account Admin 失敗", e);
            return false;
        }
    }

    @Transactional
    public boolean updateIsActive(AccountUpdateActiveRequest request, boolean isActive) {
        try {
            Account account = accountRepository.findById(request.getId())
                    .orElseThrow(() -> ExceptionUtils.cannotFindId(request.getId()));
            account.setIsActive(isActive);
            updateAuditInfo(account);
            accountRepository.save(account);
            return true;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (isActive) {
                log.error("啟用 Account 失敗", e);
            } else {
                log.error("停用 Account 失敗", e);
            }
            return false;
        }
    }

}