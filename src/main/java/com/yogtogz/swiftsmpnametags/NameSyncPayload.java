package com.yogtogz.swiftsmpnametags;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.core.UUIDUtil;
import java.util.UUID;

public record NameSyncPayload(UUID playerUUID, Component fakeName) implements CustomPacketPayload {
    public static final Type<NameSyncPayload> ID = new Type<>(Identifier.fromNamespaceAndPath("swiftsmp", "sync_names"));
    
    public static final StreamCodec<RegistryFriendlyByteBuf, NameSyncPayload> CODEC = StreamCodec.composite(
        UUIDUtil.STREAM_CODEC, NameSyncPayload::playerUUID,
        ComponentSerialization.STREAM_CODEC, NameSyncPayload::fakeName,
        NameSyncPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
