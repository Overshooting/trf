package com.gmail.aamelis.trf.ModPlayerData.ModStats.Levels;

import com.gmail.aamelis.trf.ModPlayerData.ModStats.PlayerStatData;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class PlayerLevelData {

    private int level, points;
    private double experience, maxExperience;

    public PlayerLevelData() {
        level = 0;
        points = 0;
        experience = 0;
        maxExperience = 100;
    }

    public PlayerLevelData(int level, int points, double experience, double maxExperience) {
        this.level = level;
        this.points = points;
        this.experience = experience;
        this.maxExperience = maxExperience;
    }

    public int getLevel() {
        return level;
    }

    public int getPoints() {
        return points;
    }

    public int getReadableExperience() {
        return (int)experience;
    }

    public int getReadableMaxExperience() {
        return (int)maxExperience;
    }

    public double getExperience() {
        return experience;
    }

    public double getMaxExperience() {
        return maxExperience;
    }

    public void setLevel(int newLevel, ServerPlayer player) {
        level = newLevel;
        experience = 0;
        points = (newLevel) * 2;

        PlayerStatData data = player.getData(AttachmentTypesInit.PLAYER_STATS);
        data.resetStats(player);

        maxExperience = calculateMax(level);

        setDirty(player);
    }

    private double calculateMax(int level) {
        double baseExp = 100;

        return baseExp
                + Math.pow(level, 1.5) * 25
                + Math.log(level + 1) * 50;
    }

    public void runExpCheck(ServerPlayer player) {
        if (experience < 0) {
            experience = 0;
        }

        if (maxExperience <= 0) {
            level = 0;
            experience = 0;
            maxExperience = 100;
        }


        while ((int)experience >= (int)maxExperience) {
            experience -= maxExperience;
            level++;
            points += 2;

            maxExperience = calculateMax(level);

            player.level().playSound(null, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 60.0f, 0.8f);
        }


        setDirty(player);
    }

    public void addExperience(double experienceAdded, ServerPlayer player) {
        experience += experienceAdded;

        runExpCheck(player);
    }

    public void removeExperience(double experienceRemoved, ServerPlayer player) {
        experience -= experienceRemoved;

        runExpCheck(player);
    }

    public void usePoint(ServerPlayer player) {
        if (points >= 1) {
            points--;
            setDirty(player);
        }
    }

    public void awardPoints(int amount, ServerPlayer player) {
        if (points + amount < 0) return;

        points += amount;

        setDirty(player);
    }

    private void setDirty(ServerPlayer player) {
        player.setData(AttachmentTypesInit.PLAYER_LEVEL.get(), new PlayerLevelData(level, points, experience, maxExperience));
    }

    public static final MapCodec<PlayerLevelData> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("level")
                            .forGetter(PlayerLevelData::getLevel),
                    Codec.INT.fieldOf("points")
                                    .forGetter(PlayerLevelData::getPoints),
                    Codec.DOUBLE.fieldOf("experience")
                            .forGetter(PlayerLevelData::getExperience),
                    Codec.DOUBLE.fieldOf("maxExperience")
                            .forGetter(PlayerLevelData::getMaxExperience)
            ).apply(instance, PlayerLevelData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerLevelData> STREAM_CODEC =
            StreamCodec.of(
                    (buf, data) -> {
                        buf.writeInt(data.getLevel());
                        buf.writeInt(data.getPoints());
                        buf.writeDouble(data.getExperience());
                        buf.writeDouble(data.getMaxExperience());
                    },
                    (buf) ->
                            new PlayerLevelData(buf.readInt(), buf.readInt(), buf.readDouble(), buf.readDouble())

            );

}
