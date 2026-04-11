package com.gmail.aamelis.trf.ModBlocks.ModBlockEntities;

import com.gmail.aamelis.trf.ModBlocks.LightsOutBlock;
import com.gmail.aamelis.trf.ModCommands.PresetLightsOutCommand;
import com.gmail.aamelis.trf.ModScreens.GameMasterBlockMenu;
import com.gmail.aamelis.trf.Registries.AdvancementTriggersInit;
import com.gmail.aamelis.trf.Registries.BlockEntitiesInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
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
    private String lastMessage;

    public GameMasterBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntitiesInit.GAME_MASTER_BLOCK_ENTITY.get(), pos, blockState);

        game = GameTypes.NONE;
        started = false;
        solved = false;
        lastMessage = "";
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

    public void setGame(GameTypes game) {
        this.game = game;
        setChanged();
        sync();
    }

    public String getMessage() {
        return lastMessage;
    }

    public void setMessage(String message) {
        this.lastMessage = message;
        setChanged();
        sync();
    }

    public BlockPos[] getCorners() {
        if (!validateCorners()) {
            return null;
        } else {
            return new BlockPos[]{corner1, corner2};
        }
    }

    public void startGame() throws IllegalStateException {
        switch (game) {
            case GameTypes.NONE -> throw new IllegalStateException("Cannot start unselected game!");

            case GameTypes.LIGHTS_OUT -> startLightsOut();
        }
    }

    public void resetGame() throws IllegalStateException {
        switch (game) {
            case GameTypes.NONE -> throw new IllegalStateException("Cannot reset unselected game!");

            case GameTypes.LIGHTS_OUT -> resetLightsOut();
        }
    }

    public void startLightsOut() throws IllegalStateException {
        if (started) {
            return;
        }

        if (!validateCorners()) {
            throw new IllegalStateException("Illegal Corner Combination!");
        }

        started = true;
        solved = false;

        setPreset(getLevel());

        setChanged();
        sync();
    }

    public void resetLightsOut() throws IllegalStateException {
        if (!validateCorners()) {
            throw new IllegalStateException("Illegal Corner Combination!");
        }

        setNewCorners(corner1, corner2);
        solved = false;
        started = true;

        syncRedstone();
        setChanged();
        sync();
    }

    public void setNewCorners(BlockPos pos1, BlockPos pos2) throws IllegalArgumentException {
        solved = false;
        started = false;

        if (pos1.getY() != pos2.getY()) {
            throw new IllegalArgumentException("Y coordinates must be the same!");
        }

        System.out.println("New corners being set: " + pos1.getX() + ", " + pos1.getY() + ", " + pos1.getZ() + " : " + pos2.getX() + ", " + pos2.getY() + ", " + pos2.getZ());

        corner1 = pos1;
        corner2 = pos2;

        try {
            setPreset(getLevel());
        } catch (IllegalStateException e) {
            setMessage("Corners must form a 3x3, 5x5, or 7x7 square area!");
        }

        syncRedstone();
        sync();
        setChanged();
    }

    private void setPreset(Level level) throws IllegalArgumentException {
        boolean[][] preset = parseTypeOrThrow();

        int y = corner1.getY();

        int minX = Math.min(corner1.getX(), corner2.getX());
        int minZ = Math.min(corner1.getZ(), corner2.getZ());
        int maxX = Math.max(corner1.getX(), corner2.getX());
        int maxZ = Math.max(corner1.getZ(), corner2.getZ());

        int width = maxX - minX + 1;
        int depth = maxZ - minZ + 1;

        int presetHeight = preset.length;

        for (int dz = 0; dz < depth; dz++) {
            for (int dx = 0; dx < width; dx++) {
                if (dz >= presetHeight || dx >= presetHeight) continue;

                BlockPos currentPos = new BlockPos(minX + dx, y, minZ + dz);
                BlockState state = level.getBlockState(currentPos);

                if (!(state.getBlock() instanceof LightsOutBlock)) {
                    throw new IllegalArgumentException("Non Lights Out Block detected in preset area!");
                }

                boolean value = preset[dz][dx];

                BlockState newState = state.setValue(LightsOutBlock.ACTIVATED, value);
                level.setBlock(currentPos, newState, 3);
            }
        }
    }

    private boolean[][] parseTypeOrThrow() throws IllegalStateException {
        if (!validateCorners()) {
            throw new IllegalStateException("Invalid corners set!");
        }

        int difX = Math.abs(corner1.getX() - corner2.getX());
        int difZ = Math.abs(corner1.getZ() - corner2.getZ());

        if (difX != difZ) {
            corner1 = null;
            corner2 = null;
            throw new IllegalStateException("Detected and reset illegal corner combination!");
        } else if (difX == 2) {
            return PresetLightsOutCommand.EASY_PRESET;
        } else if (difX == 4) {
            return PresetLightsOutCommand.MED_PRESET;
        } else if (difX == 6) {
            return PresetLightsOutCommand.HARD_PRESET;
        } else {
            corner1 = null;
            corner2 = null;
            throw new IllegalStateException("Detected and reset illegal corner combination!");
        }
    }

    private boolean validateCorners() {
        return corner1 != null && corner2 != null && corner1.getX() != Integer.MIN_VALUE && corner1.getY() != Integer.MIN_VALUE
                && corner1.getZ() != Integer.MIN_VALUE && corner2.getX() != Integer.MIN_VALUE && corner2.getY() != Integer.MIN_VALUE
                && corner2.getZ() != Integer.MIN_VALUE;
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
                    started = false;
                    solved = false;

                    sync();
                    setChanged();

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

                runAdvancementCheck();
                syncRedstone();
                setChanged();
                sync();
            }
        } else {
            if (solved) {
                solved = false;
                syncRedstone();
                setChanged();
                sync();
            }
        }
    }

    private void runAdvancementCheck() {
        switch (game) {
            case LIGHTS_OUT ->  {
                if (level != null && !level.isClientSide() && solved) {
                    for (ServerPlayer player : ((ServerLevel) level).players()) {
                        AdvancementTriggersInit.LIGHTS_OUT_SOLVED_TRIGGER.get().trigger(player);
                    }
                }
            }
        }
    }

    private void syncRedstone() {
        if (level == null) return;

        level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());
        level.updateNeighbourForOutputSignal(getBlockPos(), getBlockState().getBlock());
    }

    private void sync() {
        if (level instanceof ServerLevel thisLevel) {
            thisLevel.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new GameMasterBlockMenu(i, inventory, this);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        output.putString("gameType", game.name());
        output.putString("message", lastMessage);
        output.putBoolean("solved", solved);
        output.putBoolean("started", started);

        int corner1X = Integer.MIN_VALUE,
                corner1Y = corner1X,
                corner1Z = corner1X,
                corner2X = corner1X,
                corner2Y = corner1X,
                corner2Z = corner1X;

        if (corner1 != null && corner2 != null) {
            corner1X = corner1.getX();
            corner1Y = corner1.getY();
            corner1Z = corner1.getZ();

            corner2X = corner2.getX();
            corner2Y = corner2.getY();
            corner2Z = corner2.getZ();
        }

        output.putInt("cornerOneX", corner1X);
        output.putInt("cornerOneY", corner1Y);
        output.putInt("cornerOneZ", corner1Z);
        output.putInt("cornerTwoX", corner2X);
        output.putInt("cornerTwoY", corner2Y);
        output.putInt("cornerTwoZ", corner2Z);

        super.saveAdditional(output);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        game = GameTypes.valueOf(input.getStringOr("gameType", "NONE"));
        lastMessage = input.getStringOr("message", "");
        started = input.getBooleanOr("started", false);
        solved = input.getBooleanOr("solved", false);

        corner1 = new BlockPos(input.getIntOr("cornerOneX", Integer.MIN_VALUE), input.getIntOr("cornerOneY", Integer.MIN_VALUE), input.getIntOr("cornerOneZ", Integer.MIN_VALUE));
        corner2 = new BlockPos(input.getIntOr("cornerTwoX", Integer.MIN_VALUE), input.getIntOr("cornerOneY", Integer.MIN_VALUE), input.getIntOr("cornerTwoZ", Integer.MIN_VALUE));
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("You Should Not Be Seeing This!");
    }
}
