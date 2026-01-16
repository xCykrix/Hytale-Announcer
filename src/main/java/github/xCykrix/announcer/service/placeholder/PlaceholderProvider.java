package github.xCykrix.announcer.service.placeholder;

import java.util.Map;

public interface PlaceholderProvider<T> {
    String getName();

    String getUniqueIdentifier();

    String[] getIdentifiers();

    PlaceholderParameterType[] getParameters();

    Map<String, String> replacements(Map<PlaceholderParameterType, Object> parameters);
}
