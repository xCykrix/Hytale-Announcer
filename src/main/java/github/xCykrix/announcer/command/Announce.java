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
import com.hypixel.hytale.server.core.universe.world.WorldMapTracker;
import com.hypixel.hytale.server.core.universe.world.WorldMapTracker.ZoneDiscoveryInfo;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import github.xCykrix.announcer.Announcer;
import github.xCykrix.announcer.util.DateTimeDifference;
import github.xCykrix.announcer.util.MessageHelper;

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
            // Get Ref EntityStore
            Ref<EntityStore> ref = playerRef.getReference();
            if (ref == null || !ref.isValid()) {
                return;
            }
            Store<EntityStore> store = ref.getStore();
            Player player = store.getComponent(ref, Player.getComponentType());

            // Calculate Times
            WorldTimeResource worldTimeResource = ref.getStore().getResource(WorldTimeResource.getResourceType());
            LocalDateTime startDateTime = LocalDateTime.of(1, 1, 1, 0, 0);
            LocalDateTime gameDateTime = worldTimeResource.getGameDateTime();

            // Generate Regional Data
            WorldMapTracker worldMapTracker = player.getWorldMapTracker();
            ZoneDiscoveryInfo currentZone = worldMapTracker.getCurrentZone();

            // Calculate Placeholders
            List<Map.Entry<String, String>> placeholders = List.of(
                    Map.entry("world", player.getWorld().getName()),
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

            playerRef.sendMessage(MessageHelper.format(chatMessage, placeholders));
        });
    }
}
