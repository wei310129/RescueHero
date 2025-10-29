package tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.RoleDto;
import tw.com.aidenmade.rescuehero.domain.base.projection.RoleProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, RoleTypeProjectionMapper.class})
public interface RoleProjectionMapper {
    RoleProjectionMapper INSTANCE = Mappers.getMapper(RoleProjectionMapper.class);
    RoleDto toDto(RoleProjection projection);
}
