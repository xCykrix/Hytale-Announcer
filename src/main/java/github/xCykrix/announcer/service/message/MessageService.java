package github.xCykrix.announcer.service.message;

import javax.annotation.Nonnull;

import com.hypixel.hytale.server.core.Message;

import fi.sulku.hytale.TinyMsg;

public class MessageService {
    private static MessageService instance;

    private MessageService() {
    }

    public static MessageService get() {
        if (instance == null) {
            instance = new MessageService();
        }
        return instance;
    }

    @Nonnull
    public String strip(String text) {
        return text.replaceAll("(^[\"\'])|([\"\']$)", "");
    }

    @Nonnull
    public Message colorize(String text) {
        Message message = TinyMsg.parse(strip(text));
        if (message == null)
            return Message.empty();
        return message;
    }
}
