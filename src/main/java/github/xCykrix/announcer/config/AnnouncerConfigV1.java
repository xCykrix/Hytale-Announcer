package github.xCykrix.announcer.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class AnnouncerConfigV1 implements AnnouncerConfigBase {
        public static final BuilderCodec<AnnouncerConfigV1> CODEC = BuilderCodec
                        .builder(AnnouncerConfigV1.class, AnnouncerConfigV1::new)
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
                        .codecVersion(1, 1)
                        .versioned()
                        .build();

        public String greetingTitleTextTop = "Welcome";
        public String greetingTitleTextBottom = "%username%";
        public String greetingMessageText = "Please enjoy your stay on our Hytale Server!";
        public Boolean migratedFromPreviousVersion = true;

        @Override
        public Boolean getMigratedFromPreviousVersion() {
                return migratedFromPreviousVersion;
        }

}