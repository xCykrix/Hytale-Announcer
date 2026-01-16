package github.xCykrix.announcer.service.placeholder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlaceholderService {
    private static PlaceholderService instance;
    private final ConcurrentHashMap<String, PlaceholderProvider<?>> providers;

    private PlaceholderService() {
        this.providers = new ConcurrentHashMap<>();
    }

    public static PlaceholderService getInstance() {
        if (instance == null) {
            instance = new PlaceholderService();
        }
        return instance;
    }

    public void register(PlaceholderProvider<?> provider) {
        if (providers.containsKey(provider.getUniqueIdentifier())) {
            throw new IllegalArgumentException(
                    "Placeholder '" + provider.getUniqueIdentifier() + "' is already registered.");
        }
        providers.put(provider.getUniqueIdentifier(), provider);
    }

    public String parse(String text, Map<PlaceholderParameterType, Object> state) {
        for (PlaceholderProvider<?> provider : providers.values()) {
            for (PlaceholderParameterType param : provider.getParameters()) {
                if (!state.containsKey(param)) {
                    throw new IllegalArgumentException(
                            "Missing state parameter '" + param + "' required for provider '"
                                    + provider.getUniqueIdentifier()
                                    + "' when processed. Is it provided?");
                }
            }

            boolean found = false;
            for (String identifier : provider.getIdentifiers()) {
                String placeholder = "%" + identifier + "%";
                if (text.contains(placeholder)) {
                    found = true;
                }
            }
            if (!found) {
                continue;
            }

            Map<String, String> replacements = provider.replacements(state);
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                String key = "%" + entry.getKey() + "%";
                String value = entry.getValue();
                text = text.replace(key, value);
            }
        }

        return text;
    }
}
