package github.xCykrix.announcer.event;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.modules.time.WorldTimeResource;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.WorldMapTracker;
import com.hypixel.hytale.server.core.universe.world.WorldMapTracker.ZoneDiscoveryInfo;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.EventTitleUtil;

import github.xCykrix.announcer.Announcer;
import github.xCykrix.announcer.config.AnnouncerConfig;
import github.xCykrix.announcer.util.DateTimeDifference;
import github.xCykrix.announcer.util.MessageHelper;

public class GreetingPlayerReadyEvent {
    public static void onPlayerReady(PlayerReadyEvent event, AnnouncerConfig config) {
        Player player = event.getPlayer();
        Ref<EntityStore> ref = player.getReference();
        if (ref == null || !ref.isValid()) {
            return;
        }
        PlayerRef playerRef = ref.getStore().getComponent(ref, PlayerRef.getComponentType());
        if (playerRef == null) {
            return;
        }
        World world = player.getWorld();
        if (world == null) {
            return;
        }

        // Calculate Times
        WorldTimeResource worldTimeResource = ref.getStore().getResource(WorldTimeResource.getResourceType());
        LocalDateTime startDateTime = LocalDateTime.of(1, 1, 1, 0, 0);
        LocalDateTime gameDateTime = worldTimeResource.getGameDateTime();

        // Generate Regional Data
        WorldMapTracker worldMapTracker = player.getWorldMapTracker();
        ZoneDiscoveryInfo currentZone = worldMapTracker.getCurrentZone();

        // Calculate Placeholders
        List<Map.Entry<String, String>> placeholders = List.of(
                Map.entry("world", world.getName()),
                Map.entry("world_time", gameDateTime.getHour() + ":" + gameDateTime.getMinute()),
                Map.entry("world_day", String.valueOf(ChronoUnit.DAYS.between(startDateTime, gameDateTime) + 1)),
                Map.entry("online", String.valueOf(Universe.get().getPlayers().size())),

                Map.entry("username", playerRef.getUsername()),
                Map.entry("current_region", (currentZone != null) ? currentZone.regionName() : "Unknown"),
                Map.entry("current_zone", (currentZone != null) ? currentZone.zoneName() : "Unknown"),
                Map.entry("current_biome", worldMapTracker.getCurrentBiomeName()),

                Map.entry("server_uptime",
                        DateTimeDifference.getHumanReadableDuration(
                                Announcer.PLUGIN_START_TIME,
                                LocalDateTime.now())));

        // Send Title Message of the Day Greeting on Join
        world.execute(() -> {
            EventTitleUtil.showEventTitleToPlayer(
                    playerRef,
                    MessageHelper.format(config.getGreetingTitleTextBottom(), placeholders, true),
                    MessageHelper.format(config.getGreetingTitleTextTop(), placeholders, true),
                    true, (String) null, 10.0F, 1.5F, 1.5F);
        });

        // Send Welcome Message of the Day Greeting on Join
        player.sendMessage(MessageHelper.format(String.join("\n", config.getGreetingMessageText()), placeholders));
    }
}