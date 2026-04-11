package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModAttachments.PlayerSpellData;
import com.gmail.aamelis.trf.ModEntities.NPCs.Rendering.NPCRenderer;
import com.gmail.aamelis.trf.ModKeybinds.CastKeybinds;
import com.gmail.aamelis.trf.ModKeybinds.KeyInputHandler;
import com.gmail.aamelis.trf.ModUIRendering.ManaBarRenderer;
import com.gmail.aamelis.trf.ModUIRendering.SpellCastingUIRenderer;
import com.gmail.aamelis.trf.ModEntities.Projectiles.Rendering.StaffProjectileRenderer;
import com.gmail.aamelis.trf.ModScreens.GameMasterBlockScreen;
import com.gmail.aamelis.trf.Network.Packets.*;
import com.gmail.aamelis.trf.Network.ModServerPayloadHandler;
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
        event.registerEntityRenderer(
                EntitiesInit.STAFF_PROJECTILE.get(),
                StaffProjectileRenderer::new
        );

        event.registerEntityRenderer(
                EntitiesInit.STARTER_TOWN_FLAVOR_ENTITY.get(),
                NPCRenderer::new
        );
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        PlayerSpellData playerData = player.getData(AttachmentTypesInit.PLAYER_SPELL_DATA.get());

        playerData.unlockSpell("dispel", player);
        playerData.unlockSpell("shadow", player);
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
    public static void register(RegisterPayloadHandlersEvent event) {
        final var registrar = event.registrar("1");

        registrar.playToServer(
                SpellInputPacket.TYPE,
                SpellInputPacket.STREAM_CODEC,
                ModServerPayloadHandler::handleSpellInput
        );

        registrar.playToClient(
                ComboFeedbackPacket.TYPE,
                ComboFeedbackPacket.STREAM_CODEC,
                ModServerPayloadHandler::handleComboFeedback
        );
    }

    @SubscribeEvent
    public static void onRender(RenderGuiEvent.Post event) {
        SpellCastingUIRenderer.renderMessage(event);
        ManaBarRenderer.renderManaBar(event);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(MenuTypesInit.GAME_MASTER_BLOCK_MENU.get(), GameMasterBlockScreen::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(EntitiesInit.STARTER_TOWN_FLAVOR_ENTITY.get(), NPCRenderer::new);
    }
}
