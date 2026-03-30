package com.gmail.aamelis.trf.Network.Packets;

import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record BackButtonPacket(BlockPos pos) implements CustomPacketPayload {

    public static final Type<BackButtonPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "back_button_packet")
    );

    public static final StreamCodec<FriendlyByteBuf, BackButtonPacket> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    BackButtonPacket::pos,
                    BackButtonPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
