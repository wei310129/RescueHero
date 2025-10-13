package tw.com.aidenmade.rescuehero.domain.common.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.common.application.dto.RoleDto;
import tw.com.aidenmade.rescuehero.domain.common.projection.RoleProjection;
import tw.com.aidenmade.rescuehero.domain.disaster.mapstruct.DisasterProjectionMapper;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, DisasterProjectionMapper.class, RoleTypeProjectionMapper.class})
public interface RoleProjectionMapper extends BaseProjectionMapper {
    RoleProjectionMapper INSTANCE = Mappers.getMapper(RoleProjectionMapper.class);
    RoleDto toDto(RoleProjection projection);
}
