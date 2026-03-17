package com.gmail.aamelis.trf_gi;

import com.gmail.aamelis.trf_gi.Network.NetworkHandler;
import com.gmail.aamelis.trf_gi.Registries.*;
import com.google.common.graph.Network;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(TRFGearAndItemsFinalRegistry.MODID)
public class TRFGearAndItemsFinalRegistry {

    public static final String MODID = "trf_gi";

    public TRFGearAndItemsFinalRegistry(IEventBus modEventBus, ModContainer modContainer) {
        ItemsInit.register(modEventBus);
        CreativeTabsInit.register(modEventBus);
        EntitiesInit.register(modEventBus);
        AttachmentTypesInit.register(modEventBus);

        SpellsInit.register();
    }

}
