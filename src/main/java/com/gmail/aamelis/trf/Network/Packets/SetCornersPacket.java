package com.gmail.aamelis.trf.Network.Packets;

import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SetCornersPacket(BlockPos gmPos, BlockPos c1, BlockPos c2) implements CustomPacketPayload {
    public static final Type<SetCornersPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "set_corners_packet"));

    public static final StreamCodec<FriendlyByteBuf, SetCornersPacket> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, SetCornersPacket::gmPos,
                    BlockPos.STREAM_CODEC, SetCornersPacket::c1,
                    BlockPos.STREAM_CODEC, SetCornersPacket::c2,
                    SetCornersPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}