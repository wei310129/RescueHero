package tw.com.aidenmade.rescuehero.domain.common.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.RoleTypeDto;
import tw.com.aidenmade.rescuehero.domain.common.projection.RoleTypeProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class})
public interface RoleTypeProjectionMapper extends BaseProjectionMapper {
    RoleTypeProjectionMapper INSTANCE = Mappers.getMapper(RoleTypeProjectionMapper.class);
    RoleTypeDto toDto(RoleTypeProjection projection);
}
