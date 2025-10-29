package tw.com.aidenmade.rescuehero.domain.rescue.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.disaster.mapstruct.DisasterProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.rescue.application.dto.RescueGroupDto;
import tw.com.aidenmade.rescuehero.domain.rescue.projection.RescueGroupProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, DisasterProjectionMapper.class, RescueOrganizationProjectionMapper.class})
public interface RescueGroupProjectionMapper {
    RescueGroupProjectionMapper INSTANCE = Mappers.getMapper(RescueGroupProjectionMapper.class);
    RescueGroupDto toDto(RescueGroupProjection projection);
}
