package com.gmail.aamelis.trf_gi.Registries;

import com.gmail.aamelis.trf_gi.ModAttachments.PlayerSpellData;
import com.gmail.aamelis.trf_gi.ModCommands.SetMaxManaCommand;
import com.gmail.aamelis.trf_gi.ModKeybinds.CastKeybinds;
import com.gmail.aamelis.trf_gi.ModKeybinds.KeyInputHandler;
import com.gmail.aamelis.trf_gi.ModRendering.ManaBarRenderer;
import com.gmail.aamelis.trf_gi.ModRendering.SpellCastingUIRenderer;
import com.gmail.aamelis.trf_gi.ModRendering.StaffProjectileRenderer;
import com.gmail.aamelis.trf_gi.Network.ComboFeedbackPacket;
import com.gmail.aamelis.trf_gi.Network.ModServerPayloadHandler;
import com.gmail.aamelis.trf_gi.Network.SpellInputPacket;
import com.gmail.aamelis.trf_gi.TRFGearAndItemsFinalRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber(modid = TRFGearAndItemsFinalRegistry.MODID, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                EntitiesInit.STAFF_PROJECTILE.get(),
                StaffProjectileRenderer::new
        );
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        PlayerSpellData playerData = player.getData(AttachmentTypesInit.PLAYER_SPELL_DATA.get());

        playerData.setPlayerClass("mage");

        playerData.unlockSpell("dispel");
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
}
