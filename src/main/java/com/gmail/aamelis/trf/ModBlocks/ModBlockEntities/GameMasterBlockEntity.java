package com.gmail.aamelis.trf.ModBlocks.ModBlockEntities;

import com.gmail.aamelis.trf.ModBlocks.LightsOutBlock;
import com.gmail.aamelis.trf.ModCommands.PresetLightsOutCommand;
import com.gmail.aamelis.trf.ModScreens.GameMasterBlockMenu;
import com.gmail.aamelis.trf.Registries.BlockEntitiesInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
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

    private BlockPos corner1, corner2;
    private GameTypes game;
    private boolean started, solved;

    public GameMasterBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntitiesInit.GAME_MASTER_BLOCK_ENTITY.get(), pos, blockState);

        game = GameTypes.NONE;
        started = false;
        solved = false;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isSolved() {
        return solved;
    }

    public GameTypes getGame() {
        return game;
    }

    public void startLightsOut() throws IllegalAccessException {
        if (started) {
            return;
        }

        if (corner1 == null || corner2 == null) {
            throw new IllegalStateException("No corners set!");
        }

        started = true;
        solved = false;

        setChanged();
        sync();
    }

    public void resetLightsOut() throws IllegalStateException {
        if (corner1 == null || corner2 == null) {
            throw new IllegalStateException("No corners set!");
        }

        setNewCorners(corner1, corner2);
        solved = false;

        setChanged();
        sync();
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

    private void setPreset(String type, Level level) throws IllegalArgumentException {
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

    private boolean[][] matchTypeOrThrow(String type) throws IllegalArgumentException {
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

    private boolean checkSolved() {
        if (level == null) return false;

        int y = corner1.getY();

        int minX = Math.min(corner1.getX(), corner2.getX());
        int minZ = Math.min(corner1.getZ(), corner2.getZ());
        int maxX = Math.max(corner1.getX(), corner2.getX());
        int maxZ = Math.max(corner1.getZ(), corner2.getZ());

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                BlockState state = level.getBlockState(new BlockPos(x, y, z));

                if (!(state.getBlock() instanceof LightsOutBlock)) {
                    corner1 = null;
                    corner2 = null;

                    started = false;
                    solved = false;

                    return false;
                }

                if (state.getValue(LightsOutBlock.ACTIVATED)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void tick() {
        if (!started || game == GameTypes.NONE) { return; }

        if (checkSolved()) {
            if (!solved) {
                solved = true;
                setChanged();
                sync();
            }
        }
    }

    private void sync() {
        if (level instanceof ServerLevel level) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public Component getDisplayName() {
        switch (game) {
            case GameTypes.LIGHTS_OUT -> {
                return Component.translatable("block.trf.game_master_block_lights_out");
            }

            default -> {
                return Component.translatable("block.trf.game_master_block");
            }
        }
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new GameMasterBlockMenu(i, inventory, this);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        output.putString("gameType", game.name());
        output.putBoolean("solved", solved);
        output.putBoolean("started", started);

        if (corner1 != null && corner2 != null) {
            output.putInt("cornerOneX", corner1.getX());
            output.putInt("cornerOneZ", corner1.getZ());
            output.putInt("cornerTwoX", corner2.getX());
            output.putInt("cornerTwoZ", corner2.getZ());
            output.putInt("y", corner1.getY());
        }

        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        game = GameTypes.valueOf(input.getStringOr("gameType", "NONE"));
        started = input.getBooleanOr("started", false);
        solved = input.getBooleanOr("solved", false);

        int testPull = input.getIntOr("cornerOneX", Integer.MIN_VALUE);

        if (testPull != Integer.MIN_VALUE) {
            corner1 = new BlockPos(input.getInt("cornerOneX").get(), input.getInt("y").get(), input.getInt("cornerOneZ").get());
            corner2 = new BlockPos(input.getInt("cornerTwoX").get(), input.getInt("y").get(), input.getInt("cornerTwoZ").get());
        } else {
            corner1 = null;
            corner2 = null;
        }
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
