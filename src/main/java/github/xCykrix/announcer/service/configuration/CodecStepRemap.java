package github.xCykrix.announcer.service.configuration;

import com.hypixel.hytale.server.core.util.Config;

public interface CodecStepRemap<T, Z> {
    public Z remap(Config<T> current, Config<Z> next);
}
