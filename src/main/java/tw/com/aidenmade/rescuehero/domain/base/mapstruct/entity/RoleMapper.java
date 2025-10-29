package tw.com.aidenmade.rescuehero.domain.base.mapstruct.entity;

import org.mapstruct.Mapper;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.RoleDto;
import tw.com.aidenmade.rescuehero.domain.base.entity.Role;

@Mapper(componentModel = "spring", uses = {AuditInfoMapper.class, RoleTypeMapper.class})
public interface RoleMapper {
    RoleDto toDto(Role role);
}
