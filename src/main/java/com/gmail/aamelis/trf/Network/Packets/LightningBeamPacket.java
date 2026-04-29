package com.gmail.aamelis.trf.Network.Packets;

import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public record LightningBeamPacket(Vec3 start, Vec3 end, Vec3 impact) implements CustomPacketPayload {

    public static final Type<LightningBeamPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "lightning_beam_packet"));

    public static final StreamCodec<FriendlyByteBuf, LightningBeamPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, payload) -> {
                        buf.writeVec3(payload.start);
                        buf.writeVec3(payload.end);
                        buf.writeVec3(payload.impact);
                    },
                    buf -> new LightningBeamPacket(buf.readVec3(), buf.readVec3(), buf.readVec3())
            );


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
