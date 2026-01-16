package github.xCykrix.announcer.service.configuration;

import java.util.HashMap;
import java.util.LinkedList;

import com.hypixel.hytale.server.core.util.Config;

import github.xCykrix.announcer.config.AnnouncerConfigBase;
import github.xCykrix.announcer.service.configuration.remap.V1toV2;

public class CodecConfigurationService {
    public static final int LATEST_VERSION = 2;
    private static CodecConfigurationService instance;
    private HashMap<Integer, CodecStepRemap<?, ?>> REMAP_STORAGE = new HashMap<>();

    private HashMap<Integer, Config<?>> CODEC_STORAGE = new HashMap<>();

    private CodecConfigurationService() {
        REMAP_STORAGE.put(2, new V1toV2());
    }

    public static CodecConfigurationService get() {
        if (instance == null) {
            instance = new CodecConfigurationService();
        }
        return instance;
    }

    public void register(int version, Config<?> config) {
        this.CODEC_STORAGE.put(version, config);
    }

    @SuppressWarnings("unchecked")
    public Config<?> getLatestCodec() {
        // Always migrate to the latest known version regardless of the
        // version requested by the caller.
        Config<AnnouncerConfigBase> latestConfig = (Config<AnnouncerConfigBase>) this.CODEC_STORAGE
                .get(LATEST_VERSION);

        if (latestConfig == null) {
            return null;
        }

        if (!latestConfig.get().getMigratedFromPreviousVersion()) {
            // Backstep through remaps from the latest version until we find one that is
            // migrated. Then apply all remaps forward to the latest version.
            // Eg, if we have versions 1, 2, and 3, and we load version 3 but have not
            // migrated since version 1, we need to apply the
            // 1->2 remap and the 2->3 remap.

            LinkedList<Integer> versionsToRemap = new LinkedList<>();

            // Walk backwards from the latest version, collecting each version that still
            // needs to be migrated. We add them to the front of the list so that the
            // final order is ascending (1->2, 2->3, ..., N-1->N).
            for (int currentVersion = LATEST_VERSION; currentVersion > 0; currentVersion--) {
                Config<AnnouncerConfigBase> currentConfig = (Config<AnnouncerConfigBase>) this.CODEC_STORAGE
                        .get(currentVersion);

                if (currentConfig == null) {
                    continue;
                }

                if (currentConfig.get().getMigratedFromPreviousVersion()) {
                    // We've found the last version that has already been migrated from its
                    // previous version; everything after this needs to be remapped.
                    break;
                }

                versionsToRemap.addFirst(currentVersion);
            }

            // Apply each remap step in order so that data flows 1->2, 2->3, ..., N-1->N
            // until we reach the latest version.
            for (Integer targetVersion : versionsToRemap) {
                CodecStepRemap<AnnouncerConfigBase, AnnouncerConfigBase> step = (CodecStepRemap<AnnouncerConfigBase, AnnouncerConfigBase>) this.REMAP_STORAGE
                        .get(targetVersion);

                if (step == null) {
                    continue;
                }

                Config<AnnouncerConfigBase> previousConfig = (Config<AnnouncerConfigBase>) this.CODEC_STORAGE
                        .get(targetVersion - 1);
                Config<AnnouncerConfigBase> nextConfig = (Config<AnnouncerConfigBase>) this.CODEC_STORAGE
                        .get(targetVersion);

                if (previousConfig == null || nextConfig == null) {
                    continue;
                }

                step.remap(previousConfig, nextConfig);
            }

            latestConfig = (Config<AnnouncerConfigBase>) this.CODEC_STORAGE.get(LATEST_VERSION);
            latestConfig.save();
        }

        return (Config<?>) latestConfig;
    }
}
