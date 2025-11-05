package tw.com.aidenmade.rescuehero.enums;

import lombok.AllArgsConstructor;

import java.time.Duration;

@AllArgsConstructor
public enum TTLType {
    DAY_7("DAY_7", Duration.ofDays(7)),
    HOUR_1("HOUR_1", Duration.ofHours(1)),
    MINUTE_5("MINUTE_5", Duration.ofMinutes(5)),
    MINUTE_1("MINUTE_1", Duration.ofMinutes(1)),
    ;

    private final String name;
    private final Duration duration;

    @Override
    public String toString() {
        return this.name;
    }

    public long getMilliseconds() {
        return duration.toMillis();
    }

    public long getSeconds() {
        return duration.getSeconds();
    }

    public long getMinutes() {
        return duration.toMinutes();
    }

}
