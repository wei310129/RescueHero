package tw.com.aidenmade.rescuehero.common.utils;

import tw.com.aidenmade.rescuehero.common.context.TimezoneContext;

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
