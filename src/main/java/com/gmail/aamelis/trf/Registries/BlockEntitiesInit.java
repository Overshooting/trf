package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModBlocks.ModBlockEntities.GameMasterBlockEntity;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BlockEntitiesInit {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
            Registries.BLOCK_ENTITY_TYPE, TRFFinalRegistry.MODID);

    public static final Supplier<BlockEntityType<GameMasterBlockEntity>> GAME_MASTER_BLOCK_ENTITY = BLOCK_ENTITIES.register("game_master_block_entity",
            () -> new BlockEntityType<>(GameMasterBlockEntity::new, false, BlocksInit.GAME_MASTER_BLOCK.get()));

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }

}
