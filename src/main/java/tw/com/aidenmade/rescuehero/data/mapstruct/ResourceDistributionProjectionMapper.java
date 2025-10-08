package tw.com.aidenmade.rescuehero.data.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.data.dto.ResourceDistributionDto;
import tw.com.aidenmade.rescuehero.data.mapstruct.common.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.data.projection.ResourceDistributionProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, DisasterProjectionMapper.class, ResourceProjectionMapper.class, RescueTeamMemberProjectionMapper.class, UnitProjectionMapper.class, PersonProjectionMapper.class})
public interface ResourceDistributionProjectionMapper extends BaseProjectionMapper {
    ResourceDistributionProjectionMapper INSTANCE = Mappers.getMapper(ResourceDistributionProjectionMapper.class);
    ResourceDistributionDto toDto(ResourceDistributionProjection projection);
}
