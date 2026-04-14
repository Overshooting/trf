package com.gmail.aamelis.trf.Network.Packets;

import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record OpenLightsOutMenuPacket(BlockPos pos) implements CustomPacketPayload {

    public static final Type<OpenLightsOutMenuPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "open_lights_out_menu_packet")
    );

    public static final StreamCodec<FriendlyByteBuf, OpenLightsOutMenuPacket> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    OpenLightsOutMenuPacket::pos,
                    OpenLightsOutMenuPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
