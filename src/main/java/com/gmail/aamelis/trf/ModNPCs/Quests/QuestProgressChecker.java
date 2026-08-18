package com.gmail.aamelis.trf.ModNPCs.Quests;

import com.gmail.aamelis.trf.ModGlobalData.GlobalQuestData;
import com.gmail.aamelis.trf.ModNPCs.Quests.Objectives.ItemObjective;
import com.gmail.aamelis.trf.ModNPCs.Quests.Objectives.QuestObjective;
import com.gmail.aamelis.trf.ModPlayerData.QuestPlayerData.PlayerQuestData;
import com.gmail.aamelis.trf.ModPlayerData.QuestPlayerData.QuestProgress;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf.Registries.ServerModEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.Configuration;

import java.util.List;

public class QuestProgressChecker {

    public static void checkPlayerCompletion(ServerPlayer player, ResourceLocation questId, QuestLine questLine, QuestProgress progress) {
        int stageIndex = progress.getStage();

        if (stageIndex >= questLine.stages().size()) return;

        QuestStage stage = questLine.stages().get(stageIndex);

        List<QuestObjective> objectives = stage.objectives();

        boolean complete = stage.objectives().stream().allMatch(obj -> obj.isComplete(player, progress));

        if (!complete) return;

        ServerModEvents.handleBroadcast(player, stage.broadcast());

        for (QuestObjective objective : objectives) {
            if (objective instanceof ItemObjective itemObjective) {
                ItemStack required = itemObjective.getRequiredStack();

                int objectiveStackIndex = player.getInventory().findSlotMatchingItem(required);

                if (objectiveStackIndex >= 0) {
                    player.getInventory().setItem(objectiveStackIndex, new ItemStack(required.getItem(), player.getInventory().getItem(objectiveStackIndex).getCount() - required.getCount()));
                }
            }
        }

        if (!questLine.isGlobal()) {
            QuestRewardHandler.givePlayerRewards(player, stage);
        } else {
            QuestRewardHandler.giveGlobalRewards(player, stage);
        }

        progress.advanceStage();

        if (progress.getStage() >= questLine.stages().size() - 1) {
            ServerModEvents.handleBroadcast(player, questLine.broadcast());
        }
    }

}
