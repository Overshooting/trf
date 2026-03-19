package com.gmail.aamelis.trf_gi.ModAttachments;

import com.gmail.aamelis.trf_gi.Registries.AttachmentTypesInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

public class PlayerMana {

    private double manaMultiplier, maxMana, currentMana;
    private long lastUseTime;

    public PlayerMana() {
        this(100, 0.0025);
    }

    public PlayerMana(double maxMana, double manaMultiplier) {
        this.maxMana = maxMana;
        currentMana = maxMana;

        this.manaMultiplier = manaMultiplier;
    }

    public PlayerMana(double maxMana, double currentMana, double manaMultiplier, long lastCastTime) {
        this.maxMana = maxMana;
        this.currentMana = currentMana;
        this.manaMultiplier = manaMultiplier;
        this.lastUseTime = lastCastTime;
    }

    public void setMaxMana(double maxMana) {
        double oldMaxMana = this.maxMana;

        this.maxMana = maxMana;
        currentMana += maxMana - oldMaxMana;
    }

    public void setManaMultiplier(double newMultiplier) {
        manaMultiplier = newMultiplier;
    }

    public void setCurrentMana(double currentMana) {
        this.currentMana = currentMana;
    }

    public int getCurrentMana() {
        return (int)currentMana;
    }

    public int getMaxMana() {
        return (int)maxMana;
    }

    public double getManaMultiplier() {
        return manaMultiplier;
    }

    public long getLastUseTime() {
        return lastUseTime;
    }

    public void useMana(ServerPlayer player, int mana) {
        currentMana -= mana;

        lastUseTime = System.currentTimeMillis();
        setDirty(player);
    }

    public void runManaTick(ServerPlayer player) {
        if (System.currentTimeMillis() - lastUseTime < 1500) return;

        if (Math.abs(currentMana - maxMana) >= 0.1) {
            currentMana = Math.clamp(currentMana + (maxMana * manaMultiplier), 0, maxMana);

            setDirty(player);
        } else if (currentMana > maxMana) {
            currentMana = maxMana;

            setDirty(player);
        } else if (currentMana < 0) {
            currentMana = 0;

            setDirty(player);
        }
    }

    public void fillMana(ServerPlayer player) {
        if (Math.abs(currentMana - maxMana) >= 0.1) {
            currentMana = maxMana;

            setDirty(player);
        }
    }

    private void setDirty(ServerPlayer player) {
        player.setData(AttachmentTypesInit.PLAYER_MANA.get(), new PlayerMana(maxMana, currentMana, manaMultiplier, lastUseTime));
    }

    public static final MapCodec<PlayerMana> CODEC =
            RecordCodecBuilder.mapCodec(playerManaInstance ->
                    playerManaInstance.group(
                            Codec.INT.fieldOf("max_mana")
                                    .forGetter(PlayerMana::getMaxMana),
                            Codec.INT.fieldOf("current_mana")
                                    .forGetter(PlayerMana::getCurrentMana),
                            Codec.DOUBLE.fieldOf("mana_multiplier")
                                    .forGetter(PlayerMana::getManaMultiplier),
                            Codec.LONG.fieldOf("last_cast")
                                    .forGetter(PlayerMana::getLastUseTime)
                    ).apply(playerManaInstance, PlayerMana::new)
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerMana> STREAM_CODEC =
            StreamCodec.of(
                    (buf, mana) -> {
                        buf.writeDouble(mana.getMaxMana());
                        buf.writeDouble(mana.getCurrentMana());
                        buf.writeDouble(mana.getManaMultiplier());
                        buf.writeLong(mana.getLastUseTime());
                    },
                    (buf) ->
                            new PlayerMana(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readLong())

            );

}
