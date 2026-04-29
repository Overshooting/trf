package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.Network.GameMasterButtonHandler;
import com.gmail.aamelis.trf.Network.ModPayloadHandler;
import com.gmail.aamelis.trf.Network.Packets.*;
import com.gmail.aamelis.trf.Network.SpellCastingHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketsInit {

    public static void register(PayloadRegistrar registrar) {
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

        registrar.playToServer(
                SpellInputPacket.TYPE,
                SpellInputPacket.STREAM_CODEC,
                ModPayloadHandler::handleSpellInput
        );

        registrar.playToClient(
                ComboFeedbackPacket.TYPE,
                ComboFeedbackPacket.STREAM_CODEC,
                ModPayloadHandler::handleComboFeedback
        );

        registrar.playToClient(
                CooldownSyncPacket.TYPE,
                CooldownSyncPacket.STREAM_CODEC,
                ModPayloadHandler::handleCooldownSync
        );

        registrar.playToClient(
                SpellAnimationPacket.TYPE,
                SpellAnimationPacket.STREAM_CODEC,
                ModPayloadHandler::handleAnimation
        );

        registrar.playToClient(
                LightningBeamPacket.TYPE,
                LightningBeamPacket.STREAM_CODEC,
                SpellCastingHandler::handleLightningBeam
        );
    }

}
