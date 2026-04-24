package com.gmail.aamelis.trf.ModNPCs.Quests;

import com.gmail.aamelis.trf.ModAttachments.QuestAttachments.QuestProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class QuestProgressChecker {

    public static void checkCompletion(ServerPlayer player, ResourceLocation questId, QuestLine questLine, QuestProgress progress) {
        int stageIndex = progress.getStage();

        if (stageIndex >= questLine.stages().size()) return;

        QuestStage stage = questLine.stages().get(stageIndex);

        boolean complete = stage.objectives().stream().allMatch(obj -> obj.isComplete(player, progress));

        if (!complete) return;

        QuestRewardHandler.giveRewards(player, stage);

        progress.advanceStage();
    }

}
