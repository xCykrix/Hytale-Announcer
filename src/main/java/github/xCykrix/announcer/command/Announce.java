package github.xCykrix.announcer.command;

import javax.annotation.Nonnull;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.Universe;

public class Announce extends CommandBase {
    private final RequiredArg<String> chatMessage;

    public Announce(String name, String description) {
        super(name, description);
        this.chatMessage = this.withRequiredArg("chatMessage",
                "The chat message to display to all players.", ArgTypes.STRING);
        this.setPermissionGroups(new String[] { "OP" });
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        String chatMessage = (String) this.chatMessage.get(context);

        if (chatMessage.isEmpty() || chatMessage.isBlank() || chatMessage.trim().isEmpty()
                || chatMessage.trim().isBlank()) {
            context.sendMessage(Message.raw("[Announcer] Chat announcement cannot be effectively empty."));
            return;
        }

        context.sendMessage(Message.raw("[Announcer] Sending chat announcement to all players..."));
        Universe.get().getPlayers().forEach((playerRef) -> {
            playerRef.sendMessage(Message.raw(chatMessage.replaceAll("(^[\\\"\\'])|([\\\"\\']$)", "")));
        });
    }
}
