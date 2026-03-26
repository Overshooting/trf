package com.gmail.aamelis.trf.ModBlocks;

import com.gmail.aamelis.trf.ModBlocks.ModBlockEntities.GameMasterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

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



}
