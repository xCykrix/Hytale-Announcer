package github.xCykrix.announcer;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;

import github.xCykrix.announcer.command.Announce;
import github.xCykrix.announcer.command.AnnounceTitle;
import github.xCykrix.announcer.config.AnnouncerConfigV1;
import github.xCykrix.announcer.config.AnnouncerConfigV2;
import github.xCykrix.announcer.event.GreetingPlayerReadyEvent;
import github.xCykrix.announcer.service.configuration.CodecConfigurationService;
import github.xCykrix.announcer.service.placeholder.PlaceholderService;
import github.xCykrix.announcer.service.placeholder.providers.PlayerInfo;
import github.xCykrix.announcer.service.placeholder.providers.ServerUptimeInfo;
import github.xCykrix.announcer.service.placeholder.providers.WorldInfo;
import github.xCykrix.announcer.service.placeholder.providers.WorldRegionalInfo;

public class Announcer extends JavaPlugin {
    public static final LocalDateTime PLUGIN_START_TIME = LocalDateTime.now();
    private Config<AnnouncerConfigV2> CONFIG_WITH_CODEC;
    private AnnouncerConfigV2 config;

    public Announcer(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    public CompletableFuture<Void> preLoad() {
        CodecConfigurationService ccs = CodecConfigurationService.get();
        ccs.register(1, withConfig("config", AnnouncerConfigV1.CODEC));
        ccs.register(2, withConfig("config_v2", AnnouncerConfigV2.CODEC));

        return super.preLoad();
    }

    @Override
    protected void setup() {
        try {
            this.CONFIG_WITH_CODEC = (Config<AnnouncerConfigV2>) CodecConfigurationService.get().getLatestCodec();
            this.config = this.CONFIG_WITH_CODEC.get();
        } catch (Exception e) {
            this.getLogger().at(Level.SEVERE).log(
                    "Failed to load github.xCykrix_Announcer configuration file! It may be outdated, invalid, or corrupt.",
                    e);
            this.shutdown();
            return;
        }

        // Register Placeholder Providers
        PlaceholderService placeholderService = PlaceholderService.getInstance();
        placeholderService.register(PlayerInfo.INSTANCE);
        placeholderService.register(ServerUptimeInfo.INSTANCE);
        placeholderService.register(WorldInfo.INSTANCE);
        placeholderService.register(WorldRegionalInfo.INSTANCE);

        // Register Commands
        this.getCommandRegistry()
                .registerCommand(new Announce("announce", "Sends a chat announcement to all players."));
        this.getCommandRegistry().registerCommand(new AnnounceTitle("title",
                "Sends a Universe Title announcement to all players in all worlds."));

        // Register Event
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, (event) -> {
            GreetingPlayerReadyEvent.onPlayerReady(event, this.config);
        });

        super.setup();
    }

    @Override
    protected void start() {
        super.start();
    }

    @Override
    protected void shutdown() {
        super.shutdown();
    }
}
