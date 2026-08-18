package com.gmail.aamelis.trf.ModNPCs.Quests.Objectives;

import com.gmail.aamelis.trf.ModPlayerData.QuestPlayerData.QuestProgress;
import com.gmail.aamelis.trf.Registries.QuestsInit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
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

    public ItemStack getRequiredStack() {
        return new ItemStack(item, count);
    }

    @Override
    public boolean isComplete(ServerPlayer player, QuestProgress progress) {
        int desiredItemIndex = player.getInventory().findSlotMatchingItem(new ItemStack(item));

        if (desiredItemIndex == -1) return false;

        if (player.getInventory().getItem(desiredItemIndex).getItem() == item && player.getInventory().getItem(desiredItemIndex).getCount() >= count) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onItemPickup(ServerPlayer player, QuestProgress progress, ItemStack stack) {
        if (stack.getItem() == item) {
            progress.incrementItem(stack);

            System.out.println("itemPickup called for Item: " + item.getName().getString() + ", progress incremented to: " + progress.getItemCount(item));
        }
    }

    @Override
    public String type() {
        return "item";
    }

    public static void itemPickupEvent(ItemEntityPickupEvent.Post event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;

        ItemStack stack = event.getItemEntity().getItem();

        QuestsInit.forEachActiveObjective(player, (obj, progress) ->
                obj.onItemPickup(player, progress, stack));

        QuestsInit.forEachGlobalActiveObjective(player.level(), (obj, progress) ->
                obj.onItemPickup(player, progress, stack));
    }
}
