package github.xCykrix.announcer.util;

import java.time.LocalDateTime;
import java.time.Duration;

public class DateTimeDifference {
    public static String getHumanReadableDuration(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);

        // Handle negative duration (if end is before start)
        if (duration.isNegative()) {
            duration = duration.negated();
        }

        long days = duration.toDays();
        duration = duration.minusDays(days);
        long hours = duration.toHours();
        duration = duration.minusHours(hours);
        long minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);
        long seconds = duration.getSeconds();

        StringBuilder result = new StringBuilder();
        if (days > 0) {
            result.append(days).append(" days ");
        }
        if (hours > 0) {
            result.append(hours).append(" hours ");
        }
        if (minutes > 0) {
            result.append(minutes).append(" minutes ");
        }
        if (seconds > 0 || result.length() == 0) {
            result.append(seconds).append(" seconds");
        }

        return result.toString().trim();
    }
}