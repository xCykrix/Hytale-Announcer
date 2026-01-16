package github.xCykrix.announcer.util.ref;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class PlayerReferences {
    public static PlayerRef transformPlayerToPlayerRef(Player player) throws RuntimeException {
        // Placeholder for future implementation
        Ref<EntityStore> ref = player.getReference();
        if (ref == null || !ref.isValid()) {
            throw new RuntimeException("Invalid EntityStore Reference when getting PlayerRef from Player");
        }
        PlayerRef playerRef = ref.getStore().getComponent(ref,
                PlayerRef.getComponentType());
        if (playerRef == null) {
            throw new RuntimeException(
                    "PlayerRef component not found in EntityStore when getting PlayerRef from Player. Returned null instead.");
        }
        return playerRef;
    }

    public static Player transformPlayerRefToPlayer(PlayerRef playerRef) throws RuntimeException {
        Ref<EntityStore> ref = playerRef.getReference();
        if (ref == null || !ref.isValid()) {
            throw new RuntimeException("Invalid EntityStore Reference when getting Player from PlayerRef");
        }
        Store<EntityStore> store = ref.getStore();
        Player player = store.getComponent(ref, Player.getComponentType());
        if (player == null) {
            throw new RuntimeException(
                    "Player component not found in EntityStore when getting Player from PlayerRef. Returned null instead.");
        }
        return player;
    }

    public static World transformPlayerRefToPlayerWorld(PlayerRef playerRef) throws RuntimeException {
        if (playerRef.getWorldUuid() == null)
            throw new RuntimeException("PlayerRef has null World UUID when getting World from PlayerRef");
        World world = Universe.get().getWorld(playerRef.getWorldUuid());

        return world;
    }
}
