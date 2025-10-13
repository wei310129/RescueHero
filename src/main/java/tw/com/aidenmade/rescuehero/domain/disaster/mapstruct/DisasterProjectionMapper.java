package tw.com.aidenmade.rescuehero.domain.disaster.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.address.mapstruct.AddressProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.CountryProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.disaster.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.domain.common.mapstruct.BaseProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.disaster.projection.DisasterProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, CountryProjectionMapper.class, AddressProjectionMapper.class})
public interface DisasterProjectionMapper extends BaseProjectionMapper {
    DisasterProjectionMapper INSTANCE = Mappers.getMapper(DisasterProjectionMapper.class);
    DisasterDto toDto(DisasterProjection projection);
}
