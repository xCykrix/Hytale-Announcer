package github.xCykrix.announcer.config;

import javax.annotation.Nonnull;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class AnnouncerConfig {
        @Nonnull
        public static final BuilderCodec<AnnouncerConfig> CODEC = BuilderCodec
                        .builder(AnnouncerConfig.class, AnnouncerConfig::new)
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
                        .codecVersion(2, 2)
                        .versioned()
                        .build();

        private String greetingTitleTextTop = "Welcome to Hytale";
        private String greetingTitleTextBottom = "%username%";
        private String[] greetingMessageText = {
                        "<color:yellow>Welcome to our Hytale Server, %username%!</color>",
                        "<color:yellow>You are currently in: <color:blue>%current_region%</color> <color:aqua>%current_zone%</color> <color:green>%current_biome%</color>.</color>",
                        "<color:yellow>Current Game Time: <color:gold>%world_time%</color> <color:gray>(Day: %world_day%)</color>.</color>",
                        "<color:yellow>There is currently <color:gold>%online%</color> player(s) online.</color>",
                        "",
                        "<color:yellow>Server Uptime: <color:gold>%server_uptime%</color></color>"
        };

        public String getGreetingTitleTextTop() {
                return greetingTitleTextTop;
        }

        public String getGreetingTitleTextBottom() {
                return greetingTitleTextBottom;
        }

        public String[] getGreetingMessageText() {
                return greetingMessageText;
        }
}
