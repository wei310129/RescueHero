package tw.com.aidenmade.rescuehero.domain.account.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
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

    @Transactional
    public boolean register(AccountCreateRequest request) {
        try {
            Instant nowTime = Instant.now();

            // 先設 null，因為還沒註冊
            AuditInfo auditInfo = AuditInfo.builder()
                    .createdAt(nowTime)
                    .updatedAt(nowTime)
                    .createdBy(null)
                    .updatedBy(null)
                    .build();
            AuditInfo auditInfoEntity = auditInfoRepository.save(auditInfo);

            Account account = Account.builder()
                    .auditInfo(auditInfoEntity)
                    .role(Role.builder().id(request.getRoleId()).build())
                    .username(request.getUsername())
                    .passwordHash(PasswordUtils.encryptPassword(request.getPassword()))
                    .email(request.getEmail())
                    .build();
            Account accountEntity = accountRepository.save(account);

            // 註冊成功後再更新 createdBy/updatedBy 為新帳號
            auditInfoEntity.setCreatedBy(accountEntity);
            auditInfoEntity.setUpdatedBy(accountEntity);
            auditInfoRepository.save(auditInfoEntity);

            return true;
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
    public boolean updateIsActive(AccountUpdateActiveRequest request) {
        try {
            Account account = accountRepository.findById(request.getId())
                    .orElseThrow(() -> ExceptionUtils.cannotFindId(request.getId()));
            account.setIsActive(request.getIsActive());
            updateAuditInfo(account);
            accountRepository.save(account);
            return true;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("停用 Account 失敗", e);
            return false;
        }
    }

}