package com.gmail.aamelis.trf.ModNPCs.Quests.Objectives;

import com.gmail.aamelis.trf.ModPlayerData.QuestPlayerData.QuestProgress;
import com.gmail.aamelis.trf.Registries.QuestsInit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;

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

    public static void itemPickupEvent(ItemEntityPickupEvent.Post event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;

        ItemStack stack = event.getItemEntity().getItem();

        QuestsInit.forEachActiveObjective(player, (obj, progress) ->
                obj.onItemPickup(player, progress, stack));
    }
}
