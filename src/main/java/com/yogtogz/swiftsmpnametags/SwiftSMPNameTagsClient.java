package com.yogtogz.swiftsmpnametags;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SwiftSMPNametagsClient implements ClientModInitializer {
    public static final Map<UUID, Component> FAKE_NAMES = new HashMap<>();

    @Override
    public void onInitializeClient() {
        // 1. Register both payloads so the client recognizes them
        PayloadTypeRegistry.clientboundPlay().register(NameSyncPayload.ID, NameSyncPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(SkinSyncPayload.ID, SkinSyncPayload.CODEC);

        // 2. Register the receiver for Fake Names with an automatic client rendering refresh
        ClientPlayNetworking.registerGlobalReceiver(NameSyncPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                FAKE_NAMES.put(payload.playerUUID(), payload.fakeName());
                
                // STABLE FIX: To avoid version-dependent method names across 26.2 snapshots,
                // we grab the loaded player entity from the client world and force a metadata update.
                // This instantly schedules a text boundary redraw for their nametag on the next frame!
                if (context.client().level != null) {
                    Player targetPlayer = context.client().level.getPlayerByUUID(payload.playerUUID());
                    if (targetPlayer != null) {
                        // Forcing a minor display state update triggers an immediate text geometry recalculation
                        targetPlayer.setCustomNameVisible(targetPlayer.isCustomNameVisible());
                    }
                }
            });
        });

        // 3. Register the receiver for Skins (This runs the fetcher!)
        ClientPlayNetworking.registerGlobalReceiver(SkinSyncPayload.ID, (payload, context) -> {
            ClientSkinManager.fetchSkin(payload.playerUUID(), payload.skinName());
        });
    }
}
