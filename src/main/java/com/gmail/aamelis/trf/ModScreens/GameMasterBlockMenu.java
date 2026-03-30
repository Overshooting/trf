package com.gmail.aamelis.trf.ModScreens;

import com.gmail.aamelis.trf.ModBlocks.ModBlockEntities.GameMasterBlockEntity;
import com.gmail.aamelis.trf.Registries.MenuTypesInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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
    }

    public GameMasterBlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(
                blockEntity.getLevel(),
                blockEntity.getBlockPos()
        ), player, blockEntity.getBlockState().getBlock());
    }
}
