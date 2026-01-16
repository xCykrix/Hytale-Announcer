package github.xCykrix.announcer.command;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.time.WorldTimeResource;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.WorldMapTracker;
import com.hypixel.hytale.server.core.universe.world.WorldMapTracker.ZoneDiscoveryInfo;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.EventTitleUtil;

import github.xCykrix.announcer.Announcer;
import github.xCykrix.announcer.util.DateTimeDifference;
import github.xCykrix.announcer.util.MessageHelper;

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
        String title = (String) this.title.get(context);
        String subtitle = (String) this.subtitle.get(context);
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
            if (playerRef.getWorldUuid() == null)
                return;
            World world = Universe.get().getWorld(playerRef.getWorldUuid());

            // Get Ref EntityStore
            Ref<EntityStore> ref = playerRef.getReference();
            if (ref == null || !ref.isValid()) {
                return;
            }
            Store<EntityStore> store = ref.getStore();

            // Calculate Times
            WorldTimeResource worldTimeResource = ref.getStore().getResource(WorldTimeResource.getResourceType());
            LocalDateTime startDateTime = LocalDateTime.of(1, 1, 1, 0, 0);
            LocalDateTime gameDateTime = worldTimeResource.getGameDateTime();

            // Calculate Placeholders
            List<Map.Entry<String, String>> placeholders = List.of(
                    Map.entry("world", world.getName()),
                    Map.entry("world_time", gameDateTime.getHour() + ":" + gameDateTime.getMinute()),
                    Map.entry("world_day", String.valueOf(ChronoUnit.DAYS.between(startDateTime, gameDateTime) + 1)),
                    Map.entry("online", String.valueOf(Universe.get().getPlayers().size())),
                    Map.entry("username", playerRef.getUsername()),

                    Map.entry("server_uptime",
                            DateTimeDifference.getHumanReadableDuration(
                                    Announcer.PLUGIN_START_TIME,
                                    LocalDateTime.now())));

            world.execute(() -> {
                Player player = store.getComponent(ref, Player.getComponentType());

                // Generate Regional Data
                WorldMapTracker worldMapTracker = player.getWorldMapTracker();
                ZoneDiscoveryInfo currentZone = worldMapTracker.getCurrentZone();
                placeholders.addAll(List.of(
                        Map.entry("current_region", (currentZone != null) ? currentZone.regionName() : "Unknown"),
                        Map.entry("current_zone", (currentZone != null) ? currentZone.zoneName() : "Unknown"),
                        Map.entry("current_biome", worldMapTracker.getCurrentBiomeName())));

                EventTitleUtil.showEventTitleToPlayer(
                        playerRef,
                        MessageHelper.format(subtitle, placeholders, true),
                        MessageHelper.format(title, placeholders, true),
                        true, (String) null, duration, fadeIn, fadeOut);
            });
        });

    }
}
