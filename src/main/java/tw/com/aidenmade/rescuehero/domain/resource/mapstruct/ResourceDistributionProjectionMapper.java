package tw.com.aidenmade.rescuehero.domain.resource.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.PersonProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.UnitProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.disaster.mapstruct.DisasterProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.rescue.mapstruct.RescueTeamMemberProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.resource.application.dto.ResourceDistributionDto;
import tw.com.aidenmade.rescuehero.domain.resource.projection.ResourceDistributionProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, DisasterProjectionMapper.class, ResourceProjectionMapper.class, RescueTeamMemberProjectionMapper.class, UnitProjectionMapper.class, PersonProjectionMapper.class})
public interface ResourceDistributionProjectionMapper {
    ResourceDistributionProjectionMapper INSTANCE = Mappers.getMapper(ResourceDistributionProjectionMapper.class);
    ResourceDistributionDto toDto(ResourceDistributionProjection projection);
}
