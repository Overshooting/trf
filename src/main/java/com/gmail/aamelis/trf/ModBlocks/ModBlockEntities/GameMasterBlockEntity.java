package com.gmail.aamelis.trf.ModBlocks.ModBlockEntities;

import com.gmail.aamelis.trf.ModBlocks.LightsOutBlock;
import com.gmail.aamelis.trf.ModCommands.PresetLightsOutCommand;
import com.gmail.aamelis.trf.Registries.BlockEntitiesInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class GameMasterBlockEntity extends BlockEntity implements MenuProvider {

    private static final String LIGHTS_OUT_GAME_NAME = "lights_out",
            NONE = "none";

    private BlockPos corner1, corner2;
    private String game;

    public GameMasterBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntitiesInit.GAME_MASTER_BLOCK_ENTITY.get(), pos, blockState);

        game = NONE;
    }

    public void setNewCorners(BlockPos pos1, BlockPos pos2) throws IllegalArgumentException {
        if (pos1.getY() != pos2.getY()) {
            throw new IllegalArgumentException("Y coordinates must be the same!");
        }

        corner1 = pos1;
        corner2 = pos2;

        if (Math.abs(pos1.getX() - pos2.getX()) == 3 && Math.abs(pos1.getZ() - pos2.getZ()) == 3 ) {
            setPreset(PresetLightsOutCommand.EASY_TYPE, getLevel());
        } else if (Math.abs(pos1.getX() - pos2.getX()) == 5 && Math.abs(pos1.getZ() - pos2.getZ()) == 5) {
            setPreset(PresetLightsOutCommand.MED_TYPE, getLevel());
        } else if (Math.abs(pos1.getX() - pos2.getX()) == 7 && Math.abs(pos1.getZ() - pos2.getZ()) == 7) {
            setPreset(PresetLightsOutCommand.HARD_TYPE, getLevel());
        } else {
            corner1 = null;
            corner2 = null;

            throw new IllegalArgumentException("Preset area must be a 3x3, 5x5, or 7x7 square!");
        }
    }

    private void setPreset(String type, Level level) {
        boolean[][] preset = matchTypeOrThrow(type);

        int y = corner1.getY();

        int minX = Math.min(corner1.getX(), corner2.getX());
        int minZ = Math.min(corner1.getZ(), corner2.getZ());
        int maxX = Math.max(corner1.getX(), corner2.getX());
        int maxZ = Math.max(corner1.getZ(), corner2.getZ());

        int width = maxX - minX + 1;
        int depth = maxZ - minZ + 1;

        int presetHeight = preset.length;
        int presetWidth = preset[0].length;

        for (int dz = 0; dz < depth; dz++) {
            for (int dx = 0; dx < width; dx++) {
                if (dz >= presetHeight || dx >= presetWidth) continue;

                BlockPos currentPos = new BlockPos(minX + dx, y, minZ + dz);
                BlockState state = level.getBlockState(currentPos);

                if (!(state.getBlock() instanceof LightsOutBlock)) throw new IllegalArgumentException("Non Lights Out Block detected in preset area!");

                boolean value = preset[dz][dx];

                BlockState newState = state.setValue(LightsOutBlock.ACTIVATED, value);
                level.setBlock(currentPos, newState, 3);
            }
        }
    }

    private boolean[][] matchTypeOrThrow(String type) {
        switch (type) {
            case PresetLightsOutCommand.EASY_TYPE -> {
                return PresetLightsOutCommand.EASY_PRESET;
            }

            case PresetLightsOutCommand.MED_TYPE -> {
                return PresetLightsOutCommand.MED_PRESET;
            }

            case PresetLightsOutCommand.HARD_TYPE -> {
                return PresetLightsOutCommand.HARD_PRESET;
            }

            default -> {
                throw new IllegalArgumentException("Type does not match any presets");
            }
        }
    }



    @Override
    public Component getDisplayName() {
        switch (game) {
            case LIGHTS_OUT_GAME_NAME -> {
                return Component.translatable("block.trf.game_master_block_lights_out");
            }

            default -> {
                return Component.translatable("block.trf.game_master_block");
            }
        }
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return null;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        if (corner1 != null && corner2 != null) {
            output.putInt("cornerOneX", corner1.getX());
            output.putInt("cornerOneZ", corner1.getZ());
            output.putInt("cornerTwoX", corner2.getX());
            output.putInt("cornerTwoZ", corner2.getZ());
            output.putInt("y", corner1.getY());

            output.putString("game", game);
        }

        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        int y = input.getIntOr("y", throwLoadingError());
        int cornerOneX = input.getIntOr("cornerOneX", throwLoadingError());
        int cornerOneZ = input.getIntOr("cornerOneZ", throwLoadingError());
        int cornerTwoX = input.getIntOr("cornerTwoX", throwLoadingError());
        int cornerTwoZ = input.getIntOr("cornerTwoZ", throwLoadingError());
        String gameName = input.getStringOr("game", NONE);

        corner1 = new BlockPos(cornerOneX, y, cornerOneZ);
        corner2 = new BlockPos(cornerTwoX, y, cornerTwoZ);
        game = gameName;
    }

    private int throwLoadingError() {
        throw new RuntimeException("Error while loading container data for Game Master Block at: " + this.getBlockPos().getX() + ", " +
                this.getBlockPos().getY() + ", " + this.getBlockPos().getZ());
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
