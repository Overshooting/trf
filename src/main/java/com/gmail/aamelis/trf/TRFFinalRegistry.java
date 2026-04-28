package com.gmail.aamelis.trf;

import com.gmail.aamelis.trf.ModCommands.*;
import com.gmail.aamelis.trf.ModCastingSystem.SpellAnimations;
import com.gmail.aamelis.trf.Registries.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
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
        MenuTypesInit.register(modEventBus);
        BlockEntitiesInit.register(modEventBus);
        AdvancementTriggersInit.register(modEventBus);

        SpellsInit.register();

        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
        modEventBus.addListener(this::onClientSetup);
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(SetMaxManaCommand.SET_MAX_MANA_COMMAND);
        event.getDispatcher().register(PresetLightsOutCommand.LIGHTS_OUT_PRESET_COMMAND);
        event.getDispatcher().register(SetClassCommand.SET_CLASS_COMMAND);
        event.getDispatcher().register(SummonNPCCommands.SUMMON_FLAVOR_NPC_COMMAND);
        event.getDispatcher().register(SummonNPCCommands.SUMMON_STEP_QUEST_NPC_COMMAND);
        event.getDispatcher().register(SummonNPCCommands.SUMMON_TUTORIAL_STEP_QUEST_NPC_COMMAND);
        event.getDispatcher().register(SpellCommands.GIVE_SPELL_COMMAND);
        event.getDispatcher().register(SpellCommands.REVOKE_SPELL_COMMAND);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(SpellAnimations::registerFactory);
    }

}
