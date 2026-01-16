package github.xCykrix.announcer.service.placeholder.providers;

import java.time.LocalDateTime;
import java.util.Map;

import com.hypixel.hytale.server.core.universe.PlayerRef;

import github.xCykrix.announcer.Announcer;
import github.xCykrix.announcer.service.placeholder.PlaceholderParameterType;
import github.xCykrix.announcer.service.placeholder.PlaceholderProvider;
import github.xCykrix.announcer.util.DateTimeDifference;

public class ServerUptimeInfo implements PlaceholderProvider<PlayerRef> {
    public static final ServerUptimeInfo INSTANCE = new ServerUptimeInfo();

    private ServerUptimeInfo() {
    }

    @Override
    public String getName() {
        return "Server Uptime";
    }

    @Override
    public String getUniqueIdentifier() {
        return "server_uptime";
    }

    @Override
    public String[] getIdentifiers() {
        return new String[] { "server_uptime" };
    }

    @Override
    public PlaceholderParameterType[] getParameters() {
        return new PlaceholderParameterType[] {
                PlaceholderParameterType.PlayerRef,
        };
    }

    @Override
    public Map<String, String> replacements(Map<PlaceholderParameterType, Object> parameters) {
        return Map.ofEntries(
                Map.entry("server_uptime", DateTimeDifference.getHumanReadableDuration(
                        Announcer.PLUGIN_START_TIME,
                        LocalDateTime.now())));
    }
}