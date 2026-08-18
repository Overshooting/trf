package com.gmail.aamelis.trf.ModGlobalData;

import com.gmail.aamelis.trf.ModNPCs.DataLoaders.QuestCodecs;
import com.gmail.aamelis.trf.ModNPCs.Quests.QuestStage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.Map;

public class GlobalRewardData extends SavedData {

    private static final String DATA_NAME = "trf_global_quest_reward_data";

    public static final MapCodec<GlobalRewardData> MAP_CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            Codec.unboundedMap(
                                            QuestCodecs.STAGE_CODEC.codec(),
                                            Codec.INT
                                    )
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

    private final Map<QuestStage, Integer> rewards;

    public GlobalRewardData() {
        this(new HashMap<>());
    }

    public GlobalRewardData(Map<QuestStage, Integer> map) {
        rewards = new HashMap<>(map);
    }

    public static GlobalRewardData getGlobalRewardData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(TYPE);
    }

    public Map<QuestStage, Integer> getRewardSet() {
        return rewards;
    }

    public void incrementReward(QuestStage stage) {
        if (!rewards.containsKey(stage)) {
            rewards.put(stage, 1);
        } else {
            rewards.put(stage, rewards.get(stage) + 1);
        }

        setDirty();
    }

    public void removeRewards(QuestStage stage) {
        rewards.remove(stage);

        setDirty();
    }

    public void removeAllRewards() {
        rewards.clear();

        setDirty();
    }
}
