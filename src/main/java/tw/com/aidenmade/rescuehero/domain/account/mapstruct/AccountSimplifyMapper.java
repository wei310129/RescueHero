package tw.com.aidenmade.rescuehero.domain.account.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import tw.com.aidenmade.rescuehero.domain.account.entity.Account;
import tw.com.aidenmade.rescuehero.domain.account.projection.AccountProjection;

@Mapper(componentModel = "spring")
public interface AccountSimplifyMapper {
    @Named("accountToId")
    default Long toId(Account account) {
        return account == null ? null : account.getId();
    }

    @Named("accountProjectionToId")
    default Long toId(AccountProjection account) {
        return account == null ? null : account.getId();
    }
}
