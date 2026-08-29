package com.yogtogz.swiftsmpnametags;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.PlayerSkin; 
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ClientSkinManager {
    // This cache map is safe to remain here because it's a normal class, NOT a Mixin class!
    public static final Map<UUID, PlayerSkin> CUSTOM_SKINS = new HashMap<>();
    private static final UUID NIL_UUID = new UUID(0L, 0L);

    public static void fetchSkin(UUID targetId, String skinName) {
        CompletableFuture.runAsync(() -> {
            GameProfile profile = new GameProfile(NIL_UUID, skinName);
            
            // Query Mojang asynchronously for the updated 26.2 container data
            Minecraft.getInstance().getSkinManager().get(profile).thenAcceptAsync(optionalSkin -> {
                if (optionalSkin.isPresent()) {
                    PlayerSkin fetchedSkin = optionalSkin.get();
                    
                    // Safely store it into the container map on the client execution thread
                    Minecraft.getInstance().executeIfPossible(() -> {
                        CUSTOM_SKINS.put(targetId, fetchedSkin);
                    });
                }
            });
        });
    }
}
