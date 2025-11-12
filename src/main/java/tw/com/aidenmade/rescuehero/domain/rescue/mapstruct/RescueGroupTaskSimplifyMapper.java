package tw.com.aidenmade.rescuehero.domain.rescue.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import tw.com.aidenmade.rescuehero.domain.rescue.projection.RescueGroupTaskProjection;

@Mapper(componentModel = "spring")
public interface RescueGroupTaskSimplifyMapper {
    @Named("taskProjectionToId")
    default Long toId(RescueGroupTaskProjection account) {
        return account == null ? null : account.getId();
    }
}
