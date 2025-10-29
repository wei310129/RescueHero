package tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tw.com.aidenmade.rescuehero.domain.account.mapstruct.AccountSimplifyMapper;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.AuditInfoDto;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.utils.TimeMapper;
import tw.com.aidenmade.rescuehero.domain.base.projection.AuditInfoProjection;

@Mapper(componentModel = "spring", uses = {TimeMapper.class, AccountSimplifyMapper.class})
public interface AuditInfoProjectionMapper {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "accountProjectionToId")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "accountProjectionToId")
    AuditInfoDto toDto(AuditInfoProjection projection);
}
