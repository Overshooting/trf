package com.gmail.aamelis.trf.ModNPCs.Quests;

import com.gmail.aamelis.trf.ModPlayerData.ModStats.Levels.PlayerLevelData;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class QuestRewardHandler {

    public static void givePlayerRewards(ServerPlayer player, QuestStage stage) {
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

    public static void giveGlobalRewards(ServerPlayer player, QuestStage stage) {
        ServerLevel level = player.level();
        MinecraftServer server = level.getServer();

        for (ServerPlayer thisPlayer : server.getPlayerList().getPlayers()) {
            PlayerLevelData data = thisPlayer.getData(AttachmentTypesInit.PLAYER_LEVEL);
            data.addExperience(stage.experience(), thisPlayer);

            if (stage.rewardItem() != null) {
                ItemStack copy = stage.rewardItem().copy();

                boolean added = thisPlayer.getInventory().add(copy);

                if (!added) {
                    thisPlayer.drop(copy, false);
                }
            }
        }
    }

}
