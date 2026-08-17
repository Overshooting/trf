package com.gmail.aamelis.trf.ModNPCs.Quests;

import com.gmail.aamelis.trf.ModPlayerData.QuestPlayerData.QuestProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class QuestProgressChecker {

    public static void checkPlayerCompletion(ServerPlayer player, ResourceLocation questId, QuestLine questLine, QuestProgress progress) {
        int stageIndex = progress.getStage();

        if (stageIndex >= questLine.stages().size()) return;

        QuestStage stage = questLine.stages().get(stageIndex);

        boolean complete = stage.objectives().stream().allMatch(obj -> obj.isComplete(player, progress));

        if (!complete) return;

        if (!questLine.isGlobal()) {
            QuestRewardHandler.givePlayerRewards(player, stage);
        } else {
            QuestRewardHandler.giveGlobalRewards(player, stage);
        }

        progress.advanceStage();
    }

}
