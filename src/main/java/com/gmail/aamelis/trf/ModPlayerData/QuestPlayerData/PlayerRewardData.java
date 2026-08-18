package com.gmail.aamelis.trf.ModPlayerData.QuestPlayerData;

import com.gmail.aamelis.trf.ModGlobalData.GlobalRewardData;
import com.gmail.aamelis.trf.ModNPCs.Quests.QuestRewardHandler;
import com.gmail.aamelis.trf.ModNPCs.Quests.QuestStage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

public class PlayerRewardData {

    private final Map<QuestProgress, Integer> rewards;

    public PlayerRewardData() {
        this(new HashMap<>());
    }

    public PlayerRewardData(Map<QuestProgress, Integer> map) {
        rewards = new HashMap<>(map);
    }

    public Map<QuestProgress, Integer> getRewards() {
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

        }

    }

}
