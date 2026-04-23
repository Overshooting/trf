package com.gmail.aamelis.trf.Network.Packets;

import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;

public record CooldownSyncPacket(ArrayList<String> spellIds, ArrayList<Long> endTimes) implements CustomPacketPayload {

    public static final Type<CooldownSyncPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "cooldown_sync"));

    public static final StreamCodec<FriendlyByteBuf, CooldownSyncPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.STRING_UTF8),
                    CooldownSyncPacket::spellIds,
                    ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.VAR_LONG),
                    CooldownSyncPacket::endTimes,
                    CooldownSyncPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
