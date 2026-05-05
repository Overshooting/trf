package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.CastKeybinds;
import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.KeyInputHandler;
import com.gmail.aamelis.trf.ModUIRendering.ManaBarRenderer;
import com.gmail.aamelis.trf.ModUIRendering.SpellCastingUIRenderer;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;

@EventBusSubscriber(modid = TRFFinalRegistry.MODID, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        RenderersInit.registerRenderers(event);
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        CastKeybinds.register(event);
    }

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Post event) {
        KeyInputHandler.onClientTick(event);
    }

    @SubscribeEvent
    public static void onRender(RenderGuiEvent.Post event) {
        SpellCastingUIRenderer.renderMessage(event);
        ManaBarRenderer.renderManaBar(event);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        RenderersInit.registerScreens(event);
    }
}
