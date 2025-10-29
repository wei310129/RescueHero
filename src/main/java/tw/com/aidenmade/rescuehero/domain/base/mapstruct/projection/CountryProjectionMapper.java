package tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.CountryDto;
import tw.com.aidenmade.rescuehero.domain.base.projection.CountryProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class})
public interface CountryProjectionMapper {
    CountryProjectionMapper INSTANCE = Mappers.getMapper(CountryProjectionMapper.class);
    CountryDto toDto(CountryProjection projection);
}

