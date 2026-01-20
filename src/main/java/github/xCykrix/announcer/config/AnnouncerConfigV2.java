package github.xCykrix.announcer.config;

import javax.annotation.Nonnull;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class AnnouncerConfigV2 implements AnnouncerConfigBase {
        @Nonnull
        public static final BuilderCodec<AnnouncerConfigV2> CODEC = BuilderCodec
                        .builder(AnnouncerConfigV2.class, AnnouncerConfigV2::new)
                        .append(
                                        new KeyedCodec<>("GreetingTitleTextTop", Codec.STRING),
                                        (config, value) -> config.greetingTitleTextTop = value,
                                        config -> config.greetingTitleTextTop)
                        .add()
                        .append(
                                        new KeyedCodec<>("GreetingTitleTextBottom", Codec.STRING),
                                        (config, value) -> config.greetingTitleTextBottom = value,
                                        config -> config.greetingTitleTextBottom)
                        .add()
                        .append(
                                        new KeyedCodec<>("GreetingMessageText", Codec.STRING_ARRAY),
                                        (config, value) -> config.greetingMessageText = value,
                                        config -> config.greetingMessageText)
                        .add()
                        .append(
                                        new KeyedCodec<>("MigratedFromPreviousVersion", Codec.BOOLEAN),
                                        (config, value) -> config.migratedFromPreviousVersion = value,
                                        config -> config.migratedFromPreviousVersion)
                        .add()
                        .codecVersion(2, 2)
                        .versioned()
                        .build();

        public String greetingTitleTextTop = "Welcome to Hytale";
        public String greetingTitleTextBottom = "%player_username%";
        public String[] greetingMessageText = {
                        "<color:yellow>Welcome to our Hytale Server, %player_username%!</color>",
                        "<color:yellow>You are currently in: <color:blue>%world_current_region%</color> <color:aqua>%world_current_zone%</color> <color:green>%world_current_biome%</color>.</color>",
                        "<color:yellow>Current Game Time: <color:gold>%world_time%</color> <color:gray>(Day: %world_day%)</color><color:magenta>(World: %world_name%)</color>.</color>",
                        "<color:yellow>There is currently <color:gold>%players_online%</color> player(s) online.</color>",
                        "",
                        "<color:yellow>Server Uptime: <color:gold>%server_uptime%</color></color>"
        };
        public Boolean migratedFromPreviousVersion = false;

        @Override
        public Boolean getMigratedFromPreviousVersion() {
                return migratedFromPreviousVersion;
        }
}
