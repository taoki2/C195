package helper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class Time {
    /** @return the current system's date and time */
    public static LocalDateTime getLocalTime() {
        return LocalDateTime.now();
    }
    /** @return the system's zone ID */
    public static ZoneId getLocalZone() {
        return ZonedDateTime.now().getZone();
    }
    /** @return the system's zone offset */
    public static ZoneOffset getLocalZoneOffset() {
        return ZonedDateTime.now().getOffset();
    }

}
