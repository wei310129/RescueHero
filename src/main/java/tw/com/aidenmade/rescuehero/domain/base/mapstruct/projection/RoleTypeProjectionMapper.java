package tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.RoleTypeDto;
import tw.com.aidenmade.rescuehero.domain.base.projection.RoleTypeProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class})
public interface RoleTypeProjectionMapper {
    RoleTypeProjectionMapper INSTANCE = Mappers.getMapper(RoleTypeProjectionMapper.class);
    RoleTypeDto toDto(RoleTypeProjection projection);
}
