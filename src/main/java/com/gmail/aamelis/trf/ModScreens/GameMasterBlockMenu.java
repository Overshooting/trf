package com.gmail.aamelis.trf.ModScreens;

import com.gmail.aamelis.trf.ModBlocks.ModBlockEntities.GameMasterBlockEntity;
import com.gmail.aamelis.trf.Registries.MenuTypesInit;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class GameMasterBlockMenu extends AbstractContainerMenu {

    private final GameMasterBlockEntity blockEntity;
    private final Level level;

    public GameMasterBlockMenu(int containerId, Inventory inventory, FriendlyByteBuf buf) {
        this(containerId, inventory, (GameMasterBlockEntity)inventory.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public GameMasterBlockMenu(int containerId, Inventory inventory, GameMasterBlockEntity gameMasterBlockEntity) {
        super(MenuTypesInit.GAME_MASTER_BLOCK_MENU.get(), containerId);
        this.blockEntity = gameMasterBlockEntity;
        level = inventory.player.level();

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
    }

    public GameMasterBlockEntity getGameMasterBlockEntity() {
        return blockEntity;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(
                blockEntity.getLevel(),
                blockEntity.getBlockPos()
        ), player, blockEntity.getBlockState().getBlock());
    }

    private void addPlayerInventory(Inventory inv) {
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 9; k++) {
                this.addSlot(new Slot(inv, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory inv) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inv, i, 8 + i * 18, 142));
        }
    }
}
