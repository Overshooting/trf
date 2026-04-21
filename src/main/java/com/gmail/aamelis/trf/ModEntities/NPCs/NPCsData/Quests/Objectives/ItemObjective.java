package com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.Quests.Objectives;

import com.gmail.aamelis.trf.ModAttachments.QuestAttachments.QuestProgress;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemObjective implements QuestObjective{
    private final Item item;
    private final int count;

    public ItemObjective(Item item, int count) {
        this.item = item;
        this.count = count;
    }

    @Override
    public boolean isComplete(Player player, QuestProgress progress) {
        return player.getInventory().countItem(item) >= count;
    }

    @Override
    public void onItemPickup(ServerPlayer player, QuestProgress progress, ItemStack stack) {
        if (stack.getItem() == item) {
            progress.incrementItem(stack);
        }
    }
}
