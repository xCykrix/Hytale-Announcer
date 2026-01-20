package github.xCykrix.announcer.util;

import java.util.Date;
import java.text.SimpleDateFormat;

public class TimeUtils {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        seconds %= 60;
        minutes %= 60;
        hours %= 24;

        StringBuilder result = new StringBuilder();
        if (days > 0)
            result.append(days).append("d ");
        if (hours > 0 || days > 0)
            result.append(hours).append("h ");
        if (minutes > 0 || hours > 0 || days > 0)
            result.append(minutes).append("m ");
        result.append(seconds).append("s");

        return result.toString().trim();
    }

    /**
     * Format timestamp to date string
     * Example: 1704844800000 -> "2024-01-10 12:00:00"
     */
    public static String formatDate(long timestamp) {
        return DATE_FORMAT.format(new Date(timestamp));
    }
}