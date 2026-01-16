package github.xCykrix.announcer.service.placeholder.providers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.modules.time.WorldTimeResource;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import github.xCykrix.announcer.service.placeholder.PlaceholderParameterType;
import github.xCykrix.announcer.service.placeholder.PlaceholderProvider;

public class WorldInfo implements PlaceholderProvider<PlayerRef> {
    public static final WorldInfo INSTANCE = new WorldInfo();

    private WorldInfo() {
    }

    @Override
    public String getName() {
        return "World Time";
    }

    @Override
    public String getUniqueIdentifier() {
        return "world_info";
    }

    @Override
    public String[] getIdentifiers() {
        return new String[] { "world_name", "world_day", "world_time" };
    }

    @Override
    public PlaceholderParameterType[] getParameters() {
        return new PlaceholderParameterType[] {
                PlaceholderParameterType.PlayerRef,
                PlaceholderParameterType.World,
        };
    }

    @Override
    public Map<String, String> replacements(Map<PlaceholderParameterType, Object> parameters) {
        PlayerRef playerRef = parameters.get(PlaceholderParameterType.PlayerRef) instanceof PlayerRef
                ? (PlayerRef) parameters.get(PlaceholderParameterType.PlayerRef)
                : null;
        if (playerRef == null) {
            Universe.get().getLogger().atInfo()
                    .log("PlayerRef is null when getting world time placeholders.");
            return Map.ofEntries(
                    Map.entry("world_name", "null"),
                    Map.entry("world_day", "null"),
                    Map.entry("world_time", "null"));
        }
        World world = parameters.get(PlaceholderParameterType.World) instanceof World
                ? (World) parameters.get(PlaceholderParameterType.World)
                : null;
        if (world == null) {
            return Map.ofEntries(
                    Map.entry("world_name", "null"),
                    Map.entry("world_day", "null"),
                    Map.entry("world_time", "null"));
        }

        // Get Ref EntityStore
        Ref<EntityStore> ref = playerRef.getReference();
        if (ref == null || !ref.isValid()) {
            Universe.get().getLogger().atInfo()
                    .log("Ref is invalid when getting world time placeholders for player: " + playerRef.getUsername());
            return Map.ofEntries(
                    Map.entry("world_name", "null"),
                    Map.entry("world_day", "null"),
                    Map.entry("world_time", "null"));
        }

        // Calculate Times
        WorldTimeResource worldTimeResource = ref.getStore().getResource(WorldTimeResource.getResourceType());
        LocalDateTime startDateTime = LocalDateTime.of(1, 1, 1, 0, 0);
        LocalDateTime gameDateTime = worldTimeResource.getGameDateTime();

        return Map.ofEntries(
                Map.entry("world_name", world.getName()),
                Map.entry("world_day", String.valueOf(ChronoUnit.DAYS.between(startDateTime, gameDateTime) + 1)),
                Map.entry("world_time", gameDateTime.getHour() + ":" + gameDateTime.getMinute()));
    }
}
