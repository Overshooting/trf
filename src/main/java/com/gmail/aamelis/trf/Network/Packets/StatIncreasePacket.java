package com.gmail.aamelis.trf.Network.Packets;

import com.gmail.aamelis.trf.TRFFinalRegistry;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record StatIncreasePacket(String statType) implements CustomPacketPayload{

    public static final Type<StatIncreasePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "stat_increase_packet"));

    public static final StreamCodec<FriendlyByteBuf, StatIncreasePacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    StatIncreasePacket::statType,
                    StatIncreasePacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
