package github.xCykrix.announcer;

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
    private final Config<AnnouncerConfig> CONFIG_WITH_CODEC = withConfig(AnnouncerConfig.CODEC);
    private AnnouncerConfig config;

    public Announcer(@Nonnull JavaPluginInit init) {
        super(init);
        this.getDataDirectory();
    }

    @Override
    protected void setup() {
        this.config = this.CONFIG_WITH_CODEC.get();
        this.CONFIG_WITH_CODEC.save();

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
