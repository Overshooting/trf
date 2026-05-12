package com.gmail.aamelis.trf.ModNPCs.Quests;

import com.gmail.aamelis.trf.ModPlayerData.ModStats.Levels.PlayerLevelData;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class QuestRewardHandler {

    public static void giveRewards(ServerPlayer player, QuestStage stage) {
        PlayerLevelData data = player.getData(AttachmentTypesInit.PLAYER_LEVEL);
        data.addExperience(stage.experience(), player);

        if (stage.rewardItem() != null) {
            ItemStack copy = stage.rewardItem().copy();

            boolean added = player.getInventory().add(copy);

            if (!added) {
                player.drop(copy, false);
            }
        }
    }

}
