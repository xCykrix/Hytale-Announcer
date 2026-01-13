package github.xCykrix.announcer.event;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.util.EventTitleUtil;

import github.xCykrix.announcer.config.AnnouncerConfig;

public class GreetingPlayerReadyEvent {
    public static void onPlayerReady(PlayerReadyEvent event, AnnouncerConfig config) {
        Player player = event.getPlayer();
        @SuppressWarnings("removal")
        PlayerRef ref = player.getPlayerRef();

        // Send Title Message of the Day Greeting on Join
        World world = player.getWorld();
        world.execute(() -> {
            EventTitleUtil.showEventTitleToPlayer(
                    ref,
                    Message.raw(formatMessage(config.getGreetingTitleTextBottom(), ref)),
                    Message.raw(formatMessage(config.getGreetingTitleTextTop(), ref)),
                    true, (String) null, 10.0F, 1.5F, 1.5F);
        });

        // Send Welcome Message of the Day Greeting on Join
        Message message = Message.raw(formatMessage(config.getGreetingMessageText(), ref));
        player.sendMessage(message);
    }

    private static String formatMessage(String message, PlayerRef ref) {
        message = message.replace("%username%", ref.getUsername());
        return message;
    }
}