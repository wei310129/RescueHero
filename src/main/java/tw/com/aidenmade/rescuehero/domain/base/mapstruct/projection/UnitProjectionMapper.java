package tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.address.projection.AddressProjection;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.UnitDto;
import tw.com.aidenmade.rescuehero.domain.base.projection.UnitProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, CountryProjectionMapper.class, AddressProjection.class})
public interface UnitProjectionMapper {
    UnitProjectionMapper INSTANCE = Mappers.getMapper(UnitProjectionMapper.class);
    UnitDto toDto(UnitProjection projection);
}
