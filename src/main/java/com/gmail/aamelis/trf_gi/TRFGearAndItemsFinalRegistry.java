package com.gmail.aamelis.trf_gi;

import com.gmail.aamelis.trf_gi.ModCommands.SetMaxManaCommand;
import com.gmail.aamelis.trf_gi.Registries.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(TRFGearAndItemsFinalRegistry.MODID)
public class TRFGearAndItemsFinalRegistry {

    public static final String MODID = "trf_gi";

    public TRFGearAndItemsFinalRegistry(IEventBus modEventBus, ModContainer modContainer) {
        ItemsInit.register(modEventBus);
        CreativeTabsInit.register(modEventBus);
        EntitiesInit.register(modEventBus);
        AttachmentTypesInit.register(modEventBus);

        SpellsInit.register();

        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(SetMaxManaCommand.SET_MAX_MANA_COMMAND);
    }

}
