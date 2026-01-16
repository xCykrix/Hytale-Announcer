package github.xCykrix.announcer.service.configuration.remap;

import com.hypixel.hytale.server.core.util.Config;

import github.xCykrix.announcer.config.AnnouncerConfigV1;
import github.xCykrix.announcer.config.AnnouncerConfigV2;
import github.xCykrix.announcer.service.configuration.CodecStepRemap;

public class V1toV2 implements CodecStepRemap<AnnouncerConfigV1, AnnouncerConfigV2> {
    @Override
    public AnnouncerConfigV2 remap(Config<AnnouncerConfigV1> current, Config<AnnouncerConfigV2> next) {
        AnnouncerConfigV2 newConfig = next.get();

        /*
         * - No configuration changes between v1 and v2 are kept to allow for better
         * default experience.
         * - You must reconfigure Announcer to work with v2 Configuration due to
         * implementation of Color API and Placeholder Service.
         */

        newConfig.migratedFromPreviousVersion = true;

        return newConfig;
    }

}
