package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.Network.GameMasterButtonHandler;
import com.gmail.aamelis.trf.Network.Packets.*;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import java.util.Set;

@EventBusSubscriber(modid = TRFFinalRegistry.MODID)
public class ServerModEvents {

    @SubscribeEvent
    public static void playerTickEvent(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        player.getData(AttachmentTypesInit.PLAYER_MANA.get()).runManaTick(player);
    }

    @SubscribeEvent
    public static void playerRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        player.getData(AttachmentTypesInit.PLAYER_MANA.get()).fillMana(player);
    }

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final var registrar = event.registrar("1");

        registrar.playToServer(
                StartGamePacket.TYPE,
                StartGamePacket.STREAM_CODEC,
                GameMasterButtonHandler::handleStartGamePacket
        );

        registrar.playToServer(
                ResetGamePacket.TYPE,
                ResetGamePacket.STREAM_CODEC,
                GameMasterButtonHandler::handleResetGamePacket
        );

        registrar.playToServer(
                SetCornersPacket.TYPE,
                SetCornersPacket.STREAM_CODEC,
                GameMasterButtonHandler::handleSetCornersPacket
        );

        registrar.playToServer(
                SetMessagePacket.TYPE,
                SetMessagePacket.STREAM_CODEC,
                GameMasterButtonHandler::handleSetMessagePacket
        );

        registrar.playToServer(
                OpenLightsOutMenuPacket.TYPE,
                OpenLightsOutMenuPacket.STREAM_CODEC,
                GameMasterButtonHandler::handleOpenLightsOutMenu
        );

        registrar.playToServer(
                BackButtonPacket.TYPE,
                BackButtonPacket.STREAM_CODEC,
                GameMasterButtonHandler::handleBackButtonPacket
        );
    }

}
