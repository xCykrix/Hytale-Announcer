package github.xCykrix.announcer.event;

import java.util.Map;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.util.EventTitleUtil;

import github.xCykrix.announcer.config.AnnouncerConfigV2;
import github.xCykrix.announcer.service.message.MessageService;
import github.xCykrix.announcer.service.placeholder.PlaceholderParameterType;
import github.xCykrix.announcer.service.placeholder.PlaceholderService;
import github.xCykrix.announcer.util.ref.PlayerReferences;

public class GreetingPlayerReadyEvent {
    public static void onPlayerReady(PlayerReadyEvent event, AnnouncerConfigV2 config) {
        Player player = event.getPlayer();
        PlayerRef playerRef = PlayerReferences.transformPlayerToPlayerRef(player);
        World world = player.getWorld();

        Map<PlaceholderParameterType, Object> state = Map.ofEntries(
                Map.entry(PlaceholderParameterType.PlayerRef, playerRef),
                Map.entry(PlaceholderParameterType.World, world),
                Map.entry(PlaceholderParameterType.WorldMapTracker, player.getWorldMapTracker()));

        // Send Title Message of the Day Greeting on Join (Must be Message.raw as it
        // cannot contain color.)
        world.execute(() -> {
            EventTitleUtil.showEventTitleToPlayer(
                    playerRef,
                    Message.raw(PlaceholderService.getInstance().parse(config.greetingTitleTextBottom, state)),
                    Message.raw(PlaceholderService.getInstance().parse(config.greetingTitleTextTop, state)),
                    true, (String) null, 10.0F, 1.5F, 1.5F);
        });

        // Send Welcome Message of the Day Greeting on Join
        String message = PlaceholderService.getInstance().parse(
                String.join("\n", config.greetingMessageText), state);

        player.sendMessage(MessageService.get().colorize(message));
    }
}