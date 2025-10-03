package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.projection.RescueGroupTaskItemMemberRoleProjection;
import tw.com.aidenmade.rescuehero.dto.RescueGroupTaskItemMemberRoleDto;

@Mapper(componentModel = "spring", uses = {RescueGroupTaskItemProjectionMapper.class, RescueTeamMemberProjectionMapper.class, RoleProjectionMapper.class})
public interface RescueGroupTaskItemMemberRoleProjectionMapper {
    RescueGroupTaskItemMemberRoleProjectionMapper INSTANCE = Mappers.getMapper(RescueGroupTaskItemMemberRoleProjectionMapper.class);
    RescueGroupTaskItemMemberRoleDto toDto(RescueGroupTaskItemMemberRoleProjection projection);
}

