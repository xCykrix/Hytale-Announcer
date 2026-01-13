package github.example;

import javax.annotation.Nonnull;

import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import github.example.event.ExamplePlayerReadyEvent;

public class ExamplePlugin extends JavaPlugin {
    public ExamplePlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, ExamplePlayerReadyEvent::onPlayerReady);
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
