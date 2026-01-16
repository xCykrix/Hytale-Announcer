package github.xCykrix.announcer.command;

import java.util.Map;

import javax.annotation.Nonnull;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;

import github.xCykrix.announcer.service.message.MessageService;
import github.xCykrix.announcer.service.placeholder.PlaceholderParameterType;
import github.xCykrix.announcer.service.placeholder.PlaceholderService;
import github.xCykrix.announcer.util.ref.PlayerReferences;

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
        String chatMessage = MessageService.get().strip((String) this.chatMessage.get(context));

        if (chatMessage == null) {
            context.sendMessage(Message.raw("[Announcer] Chat announcement cannot be null."));
            return;
        }
        if (chatMessage.isEmpty() || chatMessage.isBlank() || chatMessage.trim().isEmpty()
                || chatMessage.trim().isBlank()) {
            context.sendMessage(Message.raw("[Announcer] Chat announcement cannot be effectively empty."));
            return;
        }

        context.sendMessage(Message.raw("[Announcer] Sending chat announcement to all players..."));
        Universe.get().getPlayers().forEach((playerRef) -> {
            World world = PlayerReferences.transformPlayerRefToPlayerWorld(playerRef);
            world.execute(() -> {
                Player player = PlayerReferences.transformPlayerRefToPlayer(playerRef);

                // Calculate Placeholders
                Map<PlaceholderParameterType, Object> state = Map.ofEntries(
                        Map.entry(PlaceholderParameterType.PlayerRef, playerRef),
                        Map.entry(PlaceholderParameterType.World, world),
                        Map.entry(PlaceholderParameterType.WorldMapTracker, player.getWorldMapTracker()));
                String message = PlaceholderService.getInstance().parse(chatMessage, state);

                playerRef.sendMessage(MessageService.get().colorize(message));
            });
        });
    }
}
