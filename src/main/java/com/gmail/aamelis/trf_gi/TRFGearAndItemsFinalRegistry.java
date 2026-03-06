package com.gmail.aamelis.trf_gi;

import com.gmail.aamelis.trf_gi.Registries.CreativeTabsInit;
import com.gmail.aamelis.trf_gi.Registries.ItemsInit;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(TRFGearAndItemsFinalRegistry.MODID)
public class TRFGearAndItemsFinalRegistry {

    public static final String MODID = "trf_gi";

    public TRFGearAndItemsFinalRegistry(IEventBus modEventBus, ModContainer modContainer) {
        ItemsInit.register(modEventBus);
        CreativeTabsInit.register(modEventBus);
    }

}
