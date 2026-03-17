package com.gmail.aamelis.trf_gi.Network;

import com.gmail.aamelis.trf_gi.ModSpells.SpellCastingSystem;
import com.gmail.aamelis.trf_gi.ModSpells.SpellInput;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkHandler {

    public static void sendSpellInput(SpellInput input) {
        ClientPacketDistributor.sendToServer(
                new SpellInputPacket(input)
        );
    }

}
