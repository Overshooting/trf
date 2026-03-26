package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModBlocks.GameMasterBlock;
import com.gmail.aamelis.trf.ModBlocks.LightsOutBlock;
import com.gmail.aamelis.trf.ModBlocks.ModBlockEntities.GameMasterBlockEntity;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlocksInit {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TRFFinalRegistry.MODID);

    public static final DeferredBlock<LightsOutBlock> LIGHTS_OUT_BLOCK = BLOCKS.registerBlock("lights_out_block",
            LightsOutBlock::new, LightsOutBlock.LIGHTS_OUT_BLOCK_PROPERTIES);

    public static final DeferredBlock<GameMasterBlock> GAME_MASTER_BLOCK = BLOCKS.registerBlock("game_master_block",
            GameMasterBlock::new, GameMasterBlock.GAME_MASTER_BLOCK_PROPERTIES);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
