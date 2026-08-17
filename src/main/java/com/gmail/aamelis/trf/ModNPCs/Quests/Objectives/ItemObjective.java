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

    @Override
    public boolean isComplete(ServerPlayer player, QuestProgress progress) {
        int totalSharedCount = 0;

        for (ServerPlayer thisPlayer : player.level().getServer().getPlayerList().getPlayers()) {
            Inventory inventory = thisPlayer.getInventory();

            for (ItemStack I : inventory.getNonEquipmentItems()) {
                if (I.getItem() == item) {
                    totalSharedCount += I.getCount();

                    if (totalSharedCount == count) break;
                }
            }

            if (totalSharedCount == count) break;
        }

        System.out.println("Complete check returned: " + (totalSharedCount >= count) + " with params: totalSharedCount: " + totalSharedCount + " and totalCount: " + count);
        return totalSharedCount >= count;
    }

    @Override
    public void onItemPickup(ServerPlayer player, QuestProgress progress, ItemStack stack) {
        if (stack.getItem() == item) {
            progress.incrementItem(stack);

            System.out.println("itemPickup called for Item: " + item.getName().getString() + ", progress incremented to: " + progress.getItemCount(item));
        }
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
