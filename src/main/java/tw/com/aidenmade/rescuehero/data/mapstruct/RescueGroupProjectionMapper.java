package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.RescueGroupProjection;
import tw.com.aidenmade.rescuehero.dto.RescueGroupDto;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, DisasterProjectionMapper.class, RescueOrganizationProjectionMapper.class})
public interface RescueGroupProjectionMapper extends BaseProjectionMapper {
    RescueGroupProjectionMapper INSTANCE = Mappers.getMapper(RescueGroupProjectionMapper.class);
    RescueGroupDto toDto(RescueGroupProjection projection);
}
