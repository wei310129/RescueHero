package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.dto.RoleDto;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.RoleProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, DisasterProjectionMapper.class, RoleTypeProjectionMapper.class})
public interface RoleProjectionMapper extends BaseProjectionMapper {
    RoleProjectionMapper INSTANCE = Mappers.getMapper(RoleProjectionMapper.class);
    RoleDto toDto(RoleProjection projection);
}
