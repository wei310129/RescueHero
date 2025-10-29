package tw.com.aidenmade.rescuehero.domain.account.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.account.api.request.AccountUpdateInsensitiveRequest;
import tw.com.aidenmade.rescuehero.domain.account.application.dto.AccountDto;
import tw.com.aidenmade.rescuehero.domain.account.entity.Account;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.entity.AuditInfoMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.entity.RoleMapper;

@Mapper(
        componentModel = "spring",
        uses = {RoleMapper.class, AuditInfoMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDto toDto(Account account);

    void updateInsensitive(@MappingTarget Account account, AccountUpdateInsensitiveRequest request);
}
