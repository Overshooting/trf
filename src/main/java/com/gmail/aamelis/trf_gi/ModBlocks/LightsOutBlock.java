package com.gmail.aamelis.trf_gi.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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

        System.out.println("Running block check");

        level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 1.0f, 1.0f);

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        for (int i = x - 1; i < x + 2; i++) {
            for (int k = z - 1; k < z + 2; k ++) {
                BlockState thisBlockState = level.getBlockState(new BlockPos(i, y, k));

                if (thisBlockState.getBlock() instanceof LightsOutBlock) {
                    boolean desiredState = !thisBlockState.getValue(ACTIVATED);

                    level.setBlock(new BlockPos(i, y, k), thisBlockState.setValue(ACTIVATED, desiredState), 3);
                    System.out.println("Block at: " + i + ", " + y + ", " + k + " detected, set block state to: " + desiredState);
                }
            }
        }

    }
}
