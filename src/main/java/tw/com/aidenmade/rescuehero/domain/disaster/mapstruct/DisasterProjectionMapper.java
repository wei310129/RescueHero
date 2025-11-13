package tw.com.aidenmade.rescuehero.domain.disaster.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.address.mapstruct.AddressMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.AuditInfoProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection.CountryProjectionMapper;
import tw.com.aidenmade.rescuehero.domain.base.mapstruct.utils.TimeMapper;
import tw.com.aidenmade.rescuehero.domain.disaster.application.dto.DisasterDto;
import tw.com.aidenmade.rescuehero.domain.disaster.projection.DisasterProjection;

@Mapper(componentModel = "spring", uses = {TimeMapper.class, AuditInfoProjectionMapper.class, CountryProjectionMapper.class, AddressMapper.class})
public interface DisasterProjectionMapper {
    DisasterProjectionMapper INSTANCE = Mappers.getMapper(DisasterProjectionMapper.class);
    DisasterDto toDto(DisasterProjection projection);
}
