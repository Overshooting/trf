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

public class PlayerLevelData {

    private int level, points;
    private double experience, maxExperience, levelMultiplier;

    public PlayerLevelData() {
        level = 1;
        points = 0;
        experience = 0;
        maxExperience = 100;
        levelMultiplier = 0.5;
    }

    public PlayerLevelData(int level, int points, double experience, double maxExperience, double levelMultiplier) {
        this.level = level;
        this.points = points;
        this.experience = experience;
        this.maxExperience = maxExperience;
        this.levelMultiplier = levelMultiplier;
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

    public double getLevelMultiplier() {
        return levelMultiplier;
    }

    public void setLevel(int newLevel, ServerPlayer player) {
        level = newLevel;
        experience = 0;
        points = (newLevel - 1) * 3;

        PlayerStatData data = player.getData(AttachmentTypesInit.PLAYER_STATS);
        data.resetStats(player);

        double[] newExpStats = calculateExpStats(level);
        maxExperience = newExpStats[0];
        levelMultiplier = newExpStats[1];

        setDirty(player);
    }

    private double[] calculateExpStats(int level) {
        double startMaxExperience = 100;
        double startMultiplier = 0.5;

        for (int i = 1; i < level; i++) {
            startMaxExperience += startMaxExperience * startMultiplier;

            if (i % 5 == 0) {
                startMultiplier += 0.1;
            }
        }

        return new double[]{startMaxExperience, startMultiplier};
    }

    public void runExpCheck(ServerPlayer player) {
        boolean changed = false;

        if (experience < 0) {
            experience = 0;
        }

        if (maxExperience <= 0) {
            level = 1;
            experience = 0;
            maxExperience = 100;
            levelMultiplier = 0.5;
        }


        while (experience >= maxExperience) {
            level++;
            points += 3;
            experience = 0;
            maxExperience += levelMultiplier * maxExperience;

            if (level % 5 == 0) {
                levelMultiplier += 0.1;
            }

            changed = true;
        }



        if (changed) setDirty(player);
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

    private void setDirty(ServerPlayer player) {
        player.setData(AttachmentTypesInit.PLAYER_LEVEL.get(), new PlayerLevelData(level, points, experience, maxExperience, levelMultiplier));
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
                            .forGetter(PlayerLevelData::getMaxExperience),
                    Codec.DOUBLE.fieldOf("levelMultiplier")
                            .forGetter(PlayerLevelData::getLevelMultiplier)
            ).apply(instance, PlayerLevelData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerLevelData> STREAM_CODEC =
            StreamCodec.of(
                    (buf, data) -> {
                        buf.writeInt(data.getLevel());
                        buf.writeInt(data.getPoints());
                        buf.writeDouble(data.getExperience());
                        buf.writeDouble(data.getMaxExperience());
                        buf.writeDouble(data.getLevelMultiplier());
                    },
                    (buf) ->
                            new PlayerLevelData(buf.readInt(), buf.readInt(), buf.readDouble(), buf.readDouble(), buf.readDouble())

            );

}
