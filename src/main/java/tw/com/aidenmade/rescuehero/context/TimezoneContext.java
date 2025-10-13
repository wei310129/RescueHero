package tw.com.aidenmade.rescuehero.context;

import java.time.ZoneId;

public class TimezoneContext {

    public static final ScopedValue<ZoneId> CURRENT_ZONE = ScopedValue.newInstance();

    public static final ZoneId DEFAULT_ZONE = ZoneId.of("UTC");

    public static ZoneId getZone() {
        return CURRENT_ZONE.isBound() ? CURRENT_ZONE.get() : DEFAULT_ZONE;
    }
}

