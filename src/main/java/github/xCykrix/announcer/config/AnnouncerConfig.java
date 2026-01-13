package github.xCykrix.announcer.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class AnnouncerConfig {
    public static final BuilderCodec<AnnouncerConfig> CODEC = BuilderCodec
            .builder(AnnouncerConfig.class, AnnouncerConfig::new)
            .append(
                    new KeyedCodec<>("Version", Codec.INTEGER),
                    (config, value) -> config.version = value,
                    config -> config.version)
            .add()
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
                    new KeyedCodec<>("GreetingMessageText", Codec.STRING),
                    (config, value) -> config.greetingMessageText = value,
                    config -> config.greetingMessageText)
            .add()
            .build();

    private Integer version = 1;
    private String greetingTitleTextTop = "Welcome";
    private String greetingTitleTextBottom = "%username%";
    private String greetingMessageText = "Please enjoy your stay on our Hytale Server!";

    public String getGreetingTitleTextTop() {
        return greetingTitleTextTop;
    }

    public String getGreetingTitleTextBottom() {
        return greetingTitleTextBottom;
    }

    public String getGreetingMessageText() {
        return greetingMessageText;
    }
}
