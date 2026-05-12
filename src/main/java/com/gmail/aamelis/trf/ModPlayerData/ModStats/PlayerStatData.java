package com.gmail.aamelis.trf.ModPlayerData.ModStats;

import com.gmail.aamelis.trf.ModPlayerData.PlayerMana;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

public class PlayerStatData {

    private int strength, constitution, magic, perception, piety;

    public PlayerStatData() {
        strength = 0;
        constitution = 0;
        magic = 0;
        perception = 0;
        piety = 0;
    }

    public PlayerStatData(int thisStrength, int thisConstitution, int thisMagic, int thisPerception, int thisPiety) {
        strength = thisStrength;
        constitution = thisConstitution;
        magic = thisMagic;
        perception = thisPerception;
        piety = thisPiety;
    }

    public int getStrength() {
        return strength;
    }

    public int getConstitution() {
        return constitution;
    }

    public int getMagic() {
        return magic;
    }

    public int getPerception() {
        return perception;
    }

    public int getPiety() {
        return piety;
    }

    public void incrementStrength(int amount, ServerPlayer player) {
        if (0 > (strength + amount)) return;

        strength += amount;

        setDirty(player);
    }

    public void incrementConstitution(int amount, ServerPlayer player) {
        if (0 > (constitution + amount)) return;

        constitution += amount;

        setDirty(player);
    }

    public void incrementMagic(int amount, ServerPlayer player) {
        if (0 > (magic + amount)) return;

        magic += amount;

        setDirty(player);
    }

    public void incrementPerception(int amount, ServerPlayer player) {
        if (0 > (perception + amount)) return;

        perception += amount;

        setDirty(player);
    }

    public void incrementPiety(int amount, ServerPlayer player) {
        if (0 > (piety + amount)) return;

        piety += amount;

        setDirty(player);
    }

    public void resetStats(ServerPlayer player) {
        strength = 0;
        constitution = 0;
        magic = 0;
        perception = 0;
        piety = 0;

        setDirty(player);
    }

    private void setDirty(ServerPlayer player) {
        player.setData(AttachmentTypesInit.PLAYER_STATS.get(), new PlayerStatData(strength, constitution, magic, perception, piety));
    }

    public static final MapCodec<PlayerStatData> CODEC = RecordCodecBuilder.mapCodec(instance -> 
            instance.group(
                    Codec.INT.fieldOf("strength")
                            .forGetter(PlayerStatData::getStrength),
                    Codec.INT.fieldOf("constitution")
                            .forGetter(PlayerStatData::getConstitution),
                    Codec.INT.fieldOf("magic")
                            .forGetter(PlayerStatData::getMagic),
                    Codec.INT.fieldOf("perception")
                            .forGetter(PlayerStatData::getPerception),
                    Codec.INT.fieldOf("piety")
                            .forGetter(PlayerStatData::getPiety)
            ).apply(instance, PlayerStatData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerStatData> STREAM_CODEC =
            StreamCodec.of(
                    (buf, stat) -> {
                        buf.writeInt(stat.getStrength());
                        buf.writeInt(stat.getConstitution());
                        buf.writeInt(stat.getMagic());
                        buf.writeInt(stat.getPerception());
                        buf.writeInt(stat.getPiety());
                    },
                    (buf) ->
                            new PlayerStatData(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt())

            );

}
