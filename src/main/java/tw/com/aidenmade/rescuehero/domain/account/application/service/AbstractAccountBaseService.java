package tw.com.aidenmade.rescuehero.domain.account.application.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tw.com.aidenmade.rescuehero.domain.account.application.dto.AccountDto;
import tw.com.aidenmade.rescuehero.domain.account.mapstruct.AccountProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.account.repository.AccountRepository;

import java.util.Optional;

@Slf4j
@Service
public class AbstractAccountBaseService {
    @NonNull
    protected final AccountRepository accountRepository;
    @NonNull
    protected final AccountProjectionMapper accountProjectionMapper;

    public AbstractAccountBaseService(AccountRepository accountRepository, AccountProjectionMapper accountProjectionMapper) {
        this.accountRepository = accountRepository;
        this.accountProjectionMapper = accountProjectionMapper;
    }

    @Transactional(readOnly = true)
    public Optional<AccountDto> getByUsername(String username) {
        return accountRepository.findProjectedByUsername(username)
                .map(accountProjectionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<AccountDto> getByEmail(String email) {
        return accountRepository.findProjectedByEmail(email)
                .map(accountProjectionMapper::toDto);
    }
}
