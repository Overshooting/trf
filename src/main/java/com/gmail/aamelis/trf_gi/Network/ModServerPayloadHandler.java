package com.gmail.aamelis.trf_gi.Network;

import com.gmail.aamelis.trf_gi.ModComboSystem.ClientComboState;
import com.gmail.aamelis.trf_gi.ModSpells.SpellCastingSystem;
import com.gmail.aamelis.trf_gi.ModSpells.SpellInput;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ModServerPayloadHandler {

    public static void handleSpellInput(SpellInputPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer)context.player();

            SpellCastingSystem.handleInput(player, payload.input());
        });
    }

    public static void handleComboFeedback(ComboFeedbackPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {

            if (payload.inputs().isEmpty()) {
                ClientComboState.currentInputs.clear();
                ClientComboState.resultMessage = "";
                return;
            }

            ClientComboState.currentInputs.clear();;

            for (int i : payload.inputs()) {
                ClientComboState.currentInputs.add(SpellInput.values()[i]);
            }

            if (payload.finished()) {
                ClientComboState.resultMessage = payload.success() ? "CAST SUCCESS" : "CAST FAILED";
                ClientComboState.resultTime = System.currentTimeMillis();
            }

            ClientComboState.lastUpdateTime = System.currentTimeMillis();
        });
    }

}
