package github.xCykrix.announcer.util;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.hypixel.hytale.server.core.Message;

import fi.sulku.hytale.TinyMsg;

public class MessageHelper {
    @Nonnull
    public static Message format(String text) {
        Message message = TinyMsg.parse(text.replaceAll("(^[\\\"\\'])|([\\\"\\']$)", ""));
        if (message == null)
            return Message.empty();
        return message;
    }

    @Nonnull
    public static Message format(String text, boolean omitColors) {
        Message message;
        if (omitColors) {
            message = Message.raw(text.replaceAll("(^[\\\"\\'])|([\\\"\\']$)", ""));
        } else {
            message = TinyMsg.parse(text.replaceAll("(^[\\\"\\'])|([\\\"\\']$)", ""));
        }
        if (message == null)
            return Message.empty();
        return message;
    }

    @Nonnull
    public static Message format(String text, List<Map.Entry<String, String>> replacements, boolean omitColors) {
        for (Map.Entry<String, String> entry : replacements) {
            text = text.replace("%" + entry.getKey() + "%", entry.getValue());
        }
        return format(text, omitColors);
    }

    @Nonnull
    public static Message format(String text, List<Map.Entry<String, String>> replacements) {
        return format(text, replacements, false);
    }
}
