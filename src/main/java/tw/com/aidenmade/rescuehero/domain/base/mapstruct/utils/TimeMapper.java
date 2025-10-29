package tw.com.aidenmade.rescuehero.domain.base.mapstruct.utils;

import org.mapstruct.Mapper;
import tw.com.aidenmade.rescuehero.context.TimezoneContext;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(componentModel = "spring")
public interface TimeMapper {
    default ZonedDateTime toZonedDateTime(Instant instant) {
        if (instant == null) return null;
        ZoneId zone = TimezoneContext.getZone(); // 用 ScopedValue/Filter 綁定的 zone
        return instant.atZone(zone);
    }
}

