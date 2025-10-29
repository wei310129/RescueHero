package tw.com.aidenmade.rescuehero.domain.account.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.account.application.dto.AccountDto;
import tw.com.aidenmade.rescuehero.domain.account.projection.AccountProjection;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.RoleProjectionMapper;

@Mapper(componentModel = "spring", uses = {RoleProjectionMapper.class, AuditInfoProjectionMapper.class})
public interface AccountProjectionMapper {
    AccountProjectionMapper INSTANCE = Mappers.getMapper(AccountProjectionMapper.class);

    AccountDto toDto(AccountProjection projection);
}


