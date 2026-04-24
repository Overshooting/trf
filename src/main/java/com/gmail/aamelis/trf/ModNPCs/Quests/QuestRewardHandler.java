package com.gmail.aamelis.trf.ModNPCs.Quests;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class QuestRewardHandler {

    public static void giveRewards(ServerPlayer player, QuestStage stage) {
        player.giveExperiencePoints(stage.experience());

        if (stage.rewardItem() != null) {
            ItemStack copy = stage.rewardItem().copy();

            boolean added = player.getInventory().add(copy);

            if (!added) {
                player.drop(copy, false);
            }
        }
    }

}
