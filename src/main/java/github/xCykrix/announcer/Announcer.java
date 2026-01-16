package github.xCykrix.announcer;

import java.time.LocalDateTime;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.util.Config;

import github.xCykrix.announcer.command.Announce;
import github.xCykrix.announcer.command.AnnounceTitle;
import github.xCykrix.announcer.config.AnnouncerConfig;
import github.xCykrix.announcer.event.GreetingPlayerReadyEvent;

public class Announcer extends JavaPlugin {
    public static final LocalDateTime PLUGIN_START_TIME = LocalDateTime.now();
    private final Config<AnnouncerConfig> CONFIG_WITH_CODEC = withConfig(AnnouncerConfig.CODEC);
    private AnnouncerConfig config;

    public Announcer(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        try {
            this.config = this.CONFIG_WITH_CODEC.get();
            this.CONFIG_WITH_CODEC.save();
        } catch (Exception e) {
            this.getLogger().at(Level.SEVERE).log(
                    "Failed to load github.xCykrix_Announcer configuration file! It may be outdated, invalid, or corrupt.",
                    e);
            this.shutdown();
            return;
        }

        // Register Commands
        this.getCommandRegistry()
                .registerCommand(new Announce("announce", "Sends a chat announcement to all players."));
        this.getCommandRegistry().registerCommand(new AnnounceTitle("title",
                "Sends a Universe Title announcement to all players in all worlds."));

        // Register Event
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, (event) -> {
            GreetingPlayerReadyEvent.onPlayerReady(event, this.config);
        });
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
