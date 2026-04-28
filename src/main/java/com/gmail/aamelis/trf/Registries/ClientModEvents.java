package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
import com.gmail.aamelis.trf.ModEntities.NPCs.Rendering.NPCRenderer;
import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.CastKeybinds;
import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.KeyInputHandler;
import com.gmail.aamelis.trf.ModUIRendering.ManaBarRenderer;
import com.gmail.aamelis.trf.ModUIRendering.SpellCastingUIRenderer;
import com.gmail.aamelis.trf.ModEntities.Projectiles.Rendering.StaffProjectileRenderer;
import com.gmail.aamelis.trf.ModScreens.GameMasterBlockScreen;
import com.gmail.aamelis.trf.Network.Packets.*;
import com.gmail.aamelis.trf.Network.ModPayloadHandler;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

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
