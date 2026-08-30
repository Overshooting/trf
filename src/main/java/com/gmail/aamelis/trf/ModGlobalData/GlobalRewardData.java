package com.gmail.aamelis.trf.ModGlobalData;

import com.gmail.aamelis.trf.ModNPCs.DataLoaders.QuestCodecs;
import com.gmail.aamelis.trf.ModNPCs.Quests.QuestStage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.Map;

public class GlobalRewardData extends SavedData {

    private static final String DATA_NAME = "trf_global_quest_reward_data";

    public static final MapCodec<GlobalRewardData> MAP_CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            Codec.INT.fieldOf("experience")
                                    .forGetter(GlobalRewardData::getExperience),
                            Codec.unboundedMap(QuestCodecs.ITEM_NAME_CODEC.codec(), Codec.INT)
                                    .fieldOf("rewards")
                                    .forGetter(data -> data.rewards)
                    ).apply(instance, GlobalRewardData::new)
            );

    public static final Codec<GlobalRewardData> CODEC = MAP_CODEC.codec();

    public static final SavedDataType<GlobalRewardData> TYPE =
            new SavedDataType<GlobalRewardData>(
                    DATA_NAME,
                    GlobalRewardData::new,
                    CODEC,
                    null
            );

    private int experience;
    private final Map<Item, Integer> rewards;

    public GlobalRewardData() {
        this(0, new HashMap<>());
    }

    public GlobalRewardData(int exp, Map<Item, Integer> map) {
        experience = exp;
        rewards = new HashMap<>(map);
    }

    public static GlobalRewardData getGlobalRewardData(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(TYPE);
    }

    public Map<Item, Integer> getItemRewardSet() {
        return rewards;
    }

    public int getExperience() {
        return experience;
    }

    public void incrementReward(int experience, ItemStack item) {
        if (experience > 0) {
            this.experience += experience;
        }

        if (item != null) {
            rewards.merge(item.getItem(), item.getCount(), Integer::sum);
        }

        setDirty();
    }

    public void removeRewards(int experience, ItemStack item) {
        if (experience > 0) {
            this.experience -= experience;

            if (this.experience < 0) {
                this.experience = 0;
            }
        }

        if (item != null) {
            rewards.merge(item.getItem(), (item.getCount()) * -1, Integer::sum);

            if (rewards.get(item) < 0) {
                rewards.remove(item);
            }
        }

        setDirty();
    }

    public void removeAllRewards() {
        experience = 0;
        rewards.clear();

        setDirty();
    }
}
