package com.yogtogz.swiftsmpnametags.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.yogtogz.swiftsmpnametags.SwiftSMPNametagsClient;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "getDisplayName", at = @At("HEAD"), cancellable = true)
    private void swiftsmp_OverrideDisplayName(CallbackInfoReturnable<Component> cir) {
        Player player = (Player) (Object) this;
        
        // This is 100% correct! It reads from the packet map to draw the name tag live
        if (SwiftSMPNametagsClient.FAKE_NAMES.containsKey(player.getUUID())) {
            Component customName = SwiftSMPNametagsClient.FAKE_NAMES.get(player.getUUID());
            if (customName != null) {
                cir.setReturnValue(customName);
            }
        }
    }
}
