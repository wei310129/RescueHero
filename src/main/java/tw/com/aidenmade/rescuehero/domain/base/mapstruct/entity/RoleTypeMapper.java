package tw.com.aidenmade.rescuehero.domain.base.mapstruct.entity;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.RoleTypeDto;
import tw.com.aidenmade.rescuehero.domain.base.entity.RoleType;

@Mapper(componentModel = "spring", uses = {AuditInfoMapper.class})
public interface RoleTypeMapper {
    RoleTypeMapper INSTANCE = Mappers.getMapper(RoleTypeMapper.class);
    RoleTypeDto toDto(RoleType entity);
}
