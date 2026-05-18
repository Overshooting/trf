package com.gmail.aamelis.trf.ModItems.DataComponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record BowCastingData(long timeCast, byte castType) {

    public static final byte NONE = 0;
    public static final byte PIERCING = 1;

    public BowCastingData() {
        this(System.currentTimeMillis(), NONE);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof BowCastingData(long cast, byte type)) && castType == type && timeCast == cast;
    }

    public static final Codec<BowCastingData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.LONG.fieldOf("time")
                            .forGetter(BowCastingData::timeCast),
                    Codec.BYTE.fieldOf("type")
                            .forGetter(BowCastingData::castType)
            ).apply(instance, BowCastingData::new)
    );

    public static final StreamCodec<FriendlyByteBuf, BowCastingData> STREAM_CODEC = StreamCodec.of(
            (buf, bowData) -> {
                buf.writeLong(bowData.timeCast);
                buf.writeByte(bowData.castType);
            },
            (buf) ->
                    new BowCastingData(buf.readLong(), buf.readByte())
    );

}
