package com.gmail.aamelis.trf.Network.Packets;

import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record RenderBowTimerPacket(long endTime, int color) implements CustomPacketPayload {

    public static final Type<RenderBowTimerPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "render_bow_timer_packet"));

    public static final StreamCodec<FriendlyByteBuf, RenderBowTimerPacket> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.LONG, RenderBowTimerPacket::endTime,
                        ByteBufCodecs.INT, RenderBowTimerPacket::color,
                        RenderBowTimerPacket::new
                );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

}
