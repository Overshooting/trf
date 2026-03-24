package com.gmail.aamelis.trf.Network;

import com.gmail.aamelis.trf.TRFFinalRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;

public record ComboFeedbackPacket(ArrayList<Integer> inputs, boolean finished, boolean success) implements CustomPacketPayload {

    public static final Type<ComboFeedbackPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "combo_feedback"));

    public static final StreamCodec<ByteBuf, ComboFeedbackPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.VAR_INT),
                    ComboFeedbackPacket::inputs,
                    ByteBufCodecs.BOOL,
                    ComboFeedbackPacket::finished,
                    ByteBufCodecs.BOOL,
                    ComboFeedbackPacket::success,
                    ComboFeedbackPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
