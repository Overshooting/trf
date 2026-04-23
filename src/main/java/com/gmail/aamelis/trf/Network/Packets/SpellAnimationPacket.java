package com.gmail.aamelis.trf.Network.Packets;

import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record SpellAnimationPacket(UUID playerId, String animationId) implements CustomPacketPayload {

    public static final Type<SpellAnimationPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "spell_anim_packet")
    );

    public static final StreamCodec<FriendlyByteBuf, SpellAnimationPacket> STREAM_CODEC =
            StreamCodec.composite(
                    UUIDUtil.STREAM_CODEC, SpellAnimationPacket::playerId,
                    ByteBufCodecs.STRING_UTF8, SpellAnimationPacket::animationId,
                    SpellAnimationPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
