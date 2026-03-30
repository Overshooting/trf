package com.gmail.aamelis.trf.ModBlocks;

import com.gmail.aamelis.trf.ModBlocks.ModBlockEntities.GameMasterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class GameMasterBlock extends Block implements EntityBlock {

    public static final Supplier<BlockBehaviour.Properties> GAME_MASTER_BLOCK_PROPERTIES = () ->
        BlockBehaviour.Properties.of().strength(0.5f).sound(SoundType.WOOD).mapColor(MapColor.COLOR_BROWN);

    public GameMasterBlock(Properties p_49224_) {
        super(p_49224_);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GameMasterBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof GameMasterBlockEntity gamemasterBlockEntity) {
                player.openMenu(gamemasterBlockEntity, pos);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof GameMasterBlockEntity gamemasterBlockEntity) {
            return gamemasterBlockEntity.isSolved() ? 15 : 0;
        }

        return 0;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : (lvl, pos, st, be) -> {
            if (be instanceof GameMasterBlockEntity gamemasterBlockEntity) {
                gamemasterBlockEntity.tick();
            }
        };
    }
}
