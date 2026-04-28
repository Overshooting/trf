package com.gmail.aamelis.trf.ModNPCs.Quests.Objectives;

import com.gmail.aamelis.trf.ModPlayerData.QuestPlayerData.QuestProgress;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface QuestObjective {
    boolean isComplete(Player player, QuestProgress progress);
    default void onKill(ServerPlayer player, QuestProgress progress, EntityType<?> type) {}
    default void onItemPickup(ServerPlayer player, QuestProgress progress, ItemStack stack) {}
    default void onTrigger(ServerPlayer player, QuestProgress progress, String triggerId) {}
}
