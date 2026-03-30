package com.gmail.aamelis.trf.Network.Packets;

import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ResetGamePacket(BlockPos pos) implements CustomPacketPayload {
    public static final Type<ResetGamePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "reset_game_packet"));

    public static final StreamCodec<FriendlyByteBuf, ResetGamePacket> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    ResetGamePacket::pos,
                    ResetGamePacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}