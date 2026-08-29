package com.yogtogz.swiftsmpnametags.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.yogtogz.swiftsmpnametags.ClientSkinManager;

import java.util.UUID;

@Mixin(PlayerInfo.class)
public abstract class PlayerInfoMixin {
    
    @Shadow public abstract GameProfile getProfile();

    @Inject(method = "getSkin", at = @At("HEAD"), cancellable = true)
    private void swiftsmp_OverrideSkin(CallbackInfoReturnable<PlayerSkin> cir) {
        UUID playerId = this.getProfile().id();

        // 1. Ensure the cache map explicitly contains a FULLY downloaded skin value
        if (ClientSkinManager.CUSTOM_SKINS.containsKey(playerId)) {
            PlayerSkin customSkin = ClientSkinManager.CUSTOM_SKINS.get(playerId);
            
            // 2. CRITICAL SAFE-GUARD: Only cancel and override if the skin asset is ready.
            // If it is null (meaning it is still downloading in the background thread),
            // this check passes right by it, avoiding the invisibility trap!
            if (customSkin != null) {
                cir.setReturnValue(customSkin);
                return;
            }
        }
    }
}
