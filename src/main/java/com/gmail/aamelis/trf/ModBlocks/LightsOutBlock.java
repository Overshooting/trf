package com.gmail.aamelis.trf.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public class LightsOutBlock extends Block {

    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    public static final Supplier<BlockBehaviour.Properties> LIGHTS_OUT_BLOCK_PROPERTIES = () ->
            BlockBehaviour.Properties.of().strength(0.5f).sound(SoundType.BASALT).mapColor(MapColor.SNOW);

    public LightsOutBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.getStateDefinition().any().setValue(ACTIVATED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVATED);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        toggleNearby(level, pos);

        return InteractionResult.CONSUME;
    }

    private void toggleNearby(Level level, BlockPos pos) {
        if (level.isClientSide()) {
            return;
        }
        level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 1.0f, 1.0f);

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        BlockPos[] blocksToCheck = {new BlockPos(x - 1, y, z), pos, new BlockPos(x + 1, y, z), new BlockPos(x, y, z - 1), new BlockPos(x, y, z + 1)};

        for (BlockPos thisPos : blocksToCheck) {
            BlockState thisState = level.getBlockState(thisPos);

            if (thisState.getBlock() instanceof LightsOutBlock) {
                boolean desiredState = !thisState.getValue(ACTIVATED);

                level.setBlock(thisPos, thisState.setValue(ACTIVATED, desiredState), 3);
            }
        }

    }
}
