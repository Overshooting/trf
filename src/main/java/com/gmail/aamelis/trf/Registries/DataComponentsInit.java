package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModItems.DataComponents.BowCastingData;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class DataComponentsInit {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, TRFFinalRegistry.MODID);

    public static final Supplier<DataComponentType<BowCastingData>> BOW_DATA = DATA_COMPONENTS.register("bow_data", () ->
            DataComponentType.<BowCastingData>builder()
                    .persistent(BowCastingData.CODEC)
                    .networkSynchronized(BowCastingData.STREAM_CODEC)
                    .build()
    );

    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
    }

}
