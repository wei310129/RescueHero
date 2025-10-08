package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.dto.RescueOrganizationDto;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.RescueOrganizationProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, DisasterProjectionMapper.class, UnitProjectionMapper.class})
public interface RescueOrganizationProjectionMapper extends BaseProjectionMapper {
    RescueOrganizationProjectionMapper INSTANCE = Mappers.getMapper(RescueOrganizationProjectionMapper.class);
    RescueOrganizationDto toDto(RescueOrganizationProjection projection);
}
