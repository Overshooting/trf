package com.gmail.aamelis.trf.ModPlayerData.QuestPlayerData;

import com.gmail.aamelis.trf.ModGlobalData.GlobalRewardData;
import com.gmail.aamelis.trf.ModNPCs.DataLoaders.QuestCodecs;
import com.gmail.aamelis.trf.ModNPCs.Quests.QuestRewardHandler;
import com.gmail.aamelis.trf.ModNPCs.Quests.QuestStage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

public class PlayerRewardData {

    private final Map<QuestStage, Integer> rewards;

    public PlayerRewardData() {
        this(new HashMap<>());
    }

    public PlayerRewardData(Map<QuestStage, Integer> map) {
        rewards = new HashMap<>(map);
    }

    public Map<QuestStage, Integer> getRewards() {
        return rewards;
    }

    public void matchRewards(ServerPlayer player) {
        ServerLevel level = player.level();
        GlobalRewardData data = level.getDataStorage().computeIfAbsent(GlobalRewardData.TYPE);
        Map<QuestStage, Integer> globalRewards = data.getRewardSet();

        for (QuestStage reward : globalRewards.keySet()) {
            int globalCount = globalRewards.get(reward);
            int playerCount = rewards.get(reward);

            for (int i = globalCount; i <= playerCount; i++) {
                QuestRewardHandler.givePlayerRewards(player, reward);
            }

            rewards.put(reward, globalCount);
        }
    }

    public void incrementRewards(QuestStage stage) {
        if (!rewards.containsKey(stage)) {
            rewards.put(stage, 1);
        } else {
            rewards.put(stage, rewards.get(stage) + 1);
        }
    }

    public static final MapCodec<PlayerRewardData> CODEC = RecordCodecBuilder.mapCodec(playerRewardDataInstance ->
            playerRewardDataInstance.group(
                    Codec.unboundedMap(QuestCodecs.STAGE_CODEC.codec(), Codec.INT)
                            .fieldOf("rewards")
                            .forGetter(data -> data.rewards)
            ).apply(playerRewardDataInstance, PlayerRewardData::new)
    );

}
