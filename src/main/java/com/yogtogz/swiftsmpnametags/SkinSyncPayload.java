package com.yogtogz.swiftsmpnametags;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.core.UUIDUtil;
import java.util.UUID;

public record SkinSyncPayload(UUID playerUUID, String skinName) implements CustomPacketPayload {
    public static final Type<SkinSyncPayload> ID = new Type<>(Identifier.fromNamespaceAndPath("swiftsmp", "sync_skins"));
    
    public static final StreamCodec<RegistryFriendlyByteBuf, SkinSyncPayload> CODEC = StreamCodec.composite(
        UUIDUtil.STREAM_CODEC, SkinSyncPayload::playerUUID,
        ByteBufCodecs.STRING_UTF8, SkinSyncPayload::skinName,
        SkinSyncPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }
}
