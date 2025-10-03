package tw.com.aidenmade.rescuehero.data.mapstruct.common;

import tw.com.aidenmade.rescuehero.context.TimezoneContext;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public interface BaseProjectionMapper {
    // 處理 Instant → ZonedDateTime
    default ZonedDateTime toZonedDateTime(Instant instant) {
        if (instant == null) return null;
        ZoneId zone = TimezoneContext.getZone(); // 用 ScopedValue/Filter 綁定的 zone
        return instant.atZone(zone);
    }
}

