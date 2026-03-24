package com.gmail.aamelis.trf;

import com.gmail.aamelis.trf.ModCommands.SetMaxManaCommand;
import com.gmail.aamelis.trf.Registries.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(TRFFinalRegistry.MODID)
public class TRFFinalRegistry {

    public static final String MODID = "trf";

    public TRFFinalRegistry(IEventBus modEventBus, ModContainer modContainer) {
        ItemsInit.register(modEventBus);
        CreativeTabsInit.register(modEventBus);
        EntitiesInit.register(modEventBus);
        AttachmentTypesInit.register(modEventBus);
        BlocksInit.register(modEventBus);

        SpellsInit.register();

        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(SetMaxManaCommand.SET_MAX_MANA_COMMAND);
    }

}
