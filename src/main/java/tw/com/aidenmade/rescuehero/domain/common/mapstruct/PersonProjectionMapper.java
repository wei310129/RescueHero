package tw.com.aidenmade.rescuehero.domain.common.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tw.com.aidenmade.rescuehero.domain.common.dto.PersonDto;
import tw.com.aidenmade.rescuehero.domain.common.projection.PersonProjection;

@Mapper(componentModel = "spring", uses = {AuditInfoProjectionMapper.class, CountryProjectionMapper.class})
public interface PersonProjectionMapper extends BaseProjectionMapper {
    PersonProjectionMapper INSTANCE = Mappers.getMapper(PersonProjectionMapper.class);
    PersonDto toDto(PersonProjection projection);
}
