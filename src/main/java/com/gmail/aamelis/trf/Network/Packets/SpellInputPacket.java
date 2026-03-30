package com.gmail.aamelis.trf.Network.Packets;

import com.gmail.aamelis.trf.ModSpells.SpellInput;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SpellInputPacket(SpellInput input) implements CustomPacketPayload {

    public static final Type<SpellInputPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "spell_input"));

    public static final StreamCodec<ByteBuf, SpellInputPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    packet -> packet.input.ordinal(),
                    i -> new SpellInputPacket(SpellInput.values()[i])
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
