package com.gmail.aamelis.trf_gi.Network;

import com.gmail.aamelis.trf_gi.ModSpells.SpellCastingSystem;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ModServerPayloadHandler {

    public static void handleSpellInput(SpellInputPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer)context.player();

            SpellCastingSystem.handleInput(player, payload.input());
        });
    }

}
