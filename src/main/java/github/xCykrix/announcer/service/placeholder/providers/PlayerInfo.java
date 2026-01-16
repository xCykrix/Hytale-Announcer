package github.xCykrix.announcer.service.placeholder.providers;

import java.util.Map;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;

import github.xCykrix.announcer.service.placeholder.PlaceholderParameterType;
import github.xCykrix.announcer.service.placeholder.PlaceholderProvider;

public class PlayerInfo implements PlaceholderProvider<PlayerRef> {
    public static final PlayerInfo INSTANCE = new PlayerInfo();

    private PlayerInfo() {
    }

    @Override
    public String getName() {
        return "Player Info";
    }

    @Override
    public String getUniqueIdentifier() {
        return "player_info";
    }

    @Override
    public String[] getIdentifiers() {
        return new String[] { "player_username", "players_online" };
    }

    @Override
    public PlaceholderParameterType[] getParameters() {
        return new PlaceholderParameterType[] {
                PlaceholderParameterType.PlayerRef,
        };
    }

    @Override
    public Map<String, String> replacements(Map<PlaceholderParameterType, Object> parameters) {
        PlayerRef playerRef = parameters.get(PlaceholderParameterType.PlayerRef) instanceof PlayerRef
                ? (PlayerRef) parameters.get(PlaceholderParameterType.PlayerRef)
                : null;
        if (playerRef == null) {
            return Map.ofEntries(
                    Map.entry("player_username", "null"),
                    Map.entry("players_online", "null"));
        }

        return Map.ofEntries(
                Map.entry("player_username", playerRef.getUsername()),
                Map.entry("players_online", String.valueOf(Universe.get().getPlayerCount())));
    }
}