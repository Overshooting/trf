package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.CastKeybinds;
import com.gmail.aamelis.trf.ModUIRendering.BowSpellRenderer;
import com.gmail.aamelis.trf.Network.KeyInputHandler;
import com.gmail.aamelis.trf.ModPlayerData.ModStats.Keybinds.StatKeybinds;
import com.gmail.aamelis.trf.ModUIRendering.ManaBarRenderer;
import com.gmail.aamelis.trf.ModUIRendering.SpellCastingUIRenderer;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
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
        StatKeybinds.register(event);
    }

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Post event) {
        KeyInputHandler.onClientTick(event);

        Window window = Minecraft.getInstance().getWindow();
        window.setTitle("Terra Regis Frigoris");
    }

    @SubscribeEvent
    public static void onRender(RenderGuiEvent.Post event) {
        SpellCastingUIRenderer.renderMessage(event);
        ManaBarRenderer.renderManaBar(event);
        BowSpellRenderer.renderTimer(event);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        RenderersInit.registerScreens(event);
    }
}
