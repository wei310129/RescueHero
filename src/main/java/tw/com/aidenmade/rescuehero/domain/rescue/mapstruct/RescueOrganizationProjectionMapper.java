package tw.com.aidenmade.rescuehero.domain.rescue.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.UnitProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.disaster.mapstruct.DisasterProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.rescue.dto.RescueOrganizationDto;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.rescue.projection.RescueOrganizationProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, DisasterProjectionMapper.class, UnitProjectionMapper.class})
public interface RescueOrganizationProjectionMapper extends BaseProjectionMapper {
    RescueOrganizationProjectionMapper INSTANCE = Mappers.getMapper(RescueOrganizationProjectionMapper.class);
    RescueOrganizationDto toDto(RescueOrganizationProjection projection);
}
