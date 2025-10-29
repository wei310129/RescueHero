package tw.com.aidenmade.rescuehero.domain.base.mapstruct.projection;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.base.application.dto.PersonDto;
import tw.com.aidenmade.rescuehero.domain.base.projection.PersonProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, CountryProjectionMapper.class})
public interface PersonProjectionMapper {
    PersonProjectionMapper INSTANCE = Mappers.getMapper(PersonProjectionMapper.class);
    PersonDto toDto(PersonProjection projection);
}
