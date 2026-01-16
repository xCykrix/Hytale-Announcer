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
import com.hypixel.hytale.server.core.util.EventTitleUtil;

import github.xCykrix.announcer.service.message.MessageService;
import github.xCykrix.announcer.service.placeholder.PlaceholderParameterType;
import github.xCykrix.announcer.service.placeholder.PlaceholderService;
import github.xCykrix.announcer.util.ref.PlayerReferences;

public class AnnounceTitle extends CommandBase {
    private final RequiredArg<String> title;
    private final RequiredArg<String> subtitle;
    private final RequiredArg<Float> duration;
    private final RequiredArg<Float> fadeIn;
    private final RequiredArg<Float> fadeOut;

    public AnnounceTitle(String name, String description) {
        super(name, description);
        this.title = this.withRequiredArg("title", "The title to display in the Universe Title.", ArgTypes.STRING);
        this.subtitle = this.withRequiredArg("subtitle", "The subtitle to display in the Universe Title.",
                ArgTypes.STRING);
        this.duration = this.withRequiredArg("duration", "The duration (in seconds) to display the Universe Title.",
                ArgTypes.FLOAT);
        this.fadeIn = this.withRequiredArg("fadeIn", "The fade-in time (in seconds) for the Universe Title.",
                ArgTypes.FLOAT);
        this.fadeOut = this.withRequiredArg("fadeOut", "The fade-out time (in seconds) for the Universe Title.",
                ArgTypes.FLOAT);
        this.setPermissionGroups(new String[] { "OP" });

    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        String title = MessageService.get().strip((String) this.title.get(context));
        String subtitle = MessageService.get().strip((String) this.subtitle.get(context));
        Float duration = (Float) this.duration.get(context);
        Float fadeIn = (Float) this.fadeIn.get(context);
        Float fadeOut = (Float) this.fadeOut.get(context);

        if (fadeIn < 0 || fadeOut < 0 || duration < 0) {
            context.sendMessage(Message.raw("[Announcer] Fade-in, fade-out, and duration must be non-negative."));
            return;
        }
        if (fadeIn > 10.0F || fadeOut > 10.0F) {
            context.sendMessage(Message.raw("[Announcer] Fade-in and fade-out cannot be greater than 10 seconds."));
            return;
        }
        if (duration > 30.0F) {
            context.sendMessage(Message.raw("[Announcer] Duration cannot be greater than 30 seconds."));
            return;
        }

        context.sendMessage(Message.raw("[Announcer] Sending Universe Title to all players..."));
        Universe.get().getPlayers().forEach((playerRef) -> {
            World world = PlayerReferences.transformPlayerRefToPlayerWorld(playerRef);

            world.execute(() -> {
                Player player = PlayerReferences.transformPlayerRefToPlayer(playerRef);

                Map<PlaceholderParameterType, Object> state = Map.ofEntries(
                        Map.entry(PlaceholderParameterType.PlayerRef, playerRef),
                        Map.entry(PlaceholderParameterType.World, world),
                        Map.entry(PlaceholderParameterType.WorldMapTracker, player.getWorldMapTracker()));

                EventTitleUtil.showEventTitleToPlayer(
                        playerRef,
                        Message.raw(PlaceholderService.getInstance().parse(subtitle, state)),
                        Message.raw(PlaceholderService.getInstance().parse(title, state)),
                        true, (String) null, duration, fadeIn, fadeOut);
            });
        });

    }
}
