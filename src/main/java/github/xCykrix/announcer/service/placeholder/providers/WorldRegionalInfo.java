package github.xCykrix.announcer.service.placeholder.providers;

import java.util.Map;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.WorldMapTracker;
import com.hypixel.hytale.server.core.universe.world.WorldMapTracker.ZoneDiscoveryInfo;

import github.xCykrix.announcer.service.placeholder.PlaceholderParameterType;
import github.xCykrix.announcer.service.placeholder.PlaceholderProvider;

public class WorldRegionalInfo implements PlaceholderProvider<PlayerRef> {
    public static final WorldRegionalInfo INSTANCE = new WorldRegionalInfo();

    private WorldRegionalInfo() {
    }

    @Override
    public String getName() {
        return "World Region Provider";
    }

    @Override
    public String getUniqueIdentifier() {
        return "world_region_provider";
    }

    @Override
    public String[] getIdentifiers() {
        return new String[] { "world_current_zone", "world_current_region", "world_current_biome" };
    }

    @Override
    public PlaceholderParameterType[] getParameters() {
        return new PlaceholderParameterType[] {
                PlaceholderParameterType.WorldMapTracker,
        };
    }

    @Override
    public Map<String, String> replacements(Map<PlaceholderParameterType, Object> parameters) {
        WorldMapTracker worldMapTracker = parameters
                .get(PlaceholderParameterType.WorldMapTracker) instanceof WorldMapTracker
                        ? (WorldMapTracker) parameters.get(PlaceholderParameterType.WorldMapTracker)
                        : null;
        if (worldMapTracker == null) {
            return Map.ofEntries(
                    Map.entry("world_current_zone", "Unknown"),
                    Map.entry("world_current_region", "Unknown"),
                    Map.entry("world_current_biome", "Unknown"));
        }
        ZoneDiscoveryInfo currentZone = worldMapTracker.getCurrentZone();

        return Map.ofEntries(
                Map.entry("world_current_zone", currentZone.zoneName()),
                Map.entry("world_current_region", currentZone.regionName()),
                Map.entry("world_current_biome", worldMapTracker.getCurrentBiomeName()));
    }
}