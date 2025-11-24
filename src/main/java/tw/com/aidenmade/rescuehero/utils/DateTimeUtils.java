package tw.com.aidenmade.rescuehero.utils;

import tw.com.aidenmade.rescuehero.config.data.TimezoneContext;

import java.time.Instant;
import java.time.ZonedDateTime;

public class DateTimeUtils {

    public DateTimeUtils() {
        throw new AssertionError("No DateTimeUtils instances for you!");
    }

    public static ZonedDateTime convertInstantToZonedDateTime(Instant instant) {
        return instant == null ? null : ZonedDateTime.ofInstant(instant, TimezoneContext.getZone());
    }
}
