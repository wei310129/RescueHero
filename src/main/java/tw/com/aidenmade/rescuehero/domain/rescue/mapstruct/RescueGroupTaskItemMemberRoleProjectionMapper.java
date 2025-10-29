package tw.com.aidenmade.rescuehero.domain.rescue.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.RoleProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueGroupTaskItemMemberRoleDto;
import tw.com.aidenmade.rescuehero.domain.rescue.projection.RescueGroupTaskItemMemberRoleProjection;

@Mapper(componentModel = "spring", uses = {RescueGroupTaskItemMapper.class, RescueTeamMemberProjectionMapper.class, RoleProjectionMapper.class})
public interface RescueGroupTaskItemMemberRoleProjectionMapper {
    RescueGroupTaskItemMemberRoleProjectionMapper INSTANCE = Mappers.getMapper(RescueGroupTaskItemMemberRoleProjectionMapper.class);
    RescueGroupTaskItemMemberRoleDto toDto(RescueGroupTaskItemMemberRoleProjection projection);
}

