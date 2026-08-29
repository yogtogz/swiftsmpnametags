package com.yogtogz.swiftsmpnametags.mixin;

import net.minecraft.client.multiplayer.AccountProfileKeyPairManager;
import net.minecraft.world.entity.player.ProfileKeyPair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Mixin(AccountProfileKeyPairManager.class)
public class AccountProfileKeyPairManagerMixin {

    @Inject(method = "prepareKeyPair", at = @At("HEAD"), cancellable = true)
    private void swiftsmp$skipKeyPairFetch(CallbackInfoReturnable<CompletableFuture<Optional<ProfileKeyPair>>> cir) {
        // Force the client to return an empty profile container instantly.
        // This completely bypasses the crashing Mojang API web calls on LAN worlds!
        cir.setReturnValue(CompletableFuture.completedFuture(Optional.empty()));
    }
}
