package com.gmail.aamelis.trf_gi.Registries;

import com.gmail.aamelis.trf_gi.ModBlocks.LightsOutBlock;
import com.gmail.aamelis.trf_gi.TRFGearAndItemsFinalRegistry;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlocksInit {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TRFGearAndItemsFinalRegistry.MODID);

    public static final DeferredBlock<Block> LIGHTS_OUT_BLOCK = BLOCKS.registerBlock("lights_out_block",
            LightsOutBlock::new, LightsOutBlock.LIGHTS_OUT_BLOCK_PROPERTIES);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
