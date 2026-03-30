package com.gmail.aamelis.trf.Network;

import com.gmail.aamelis.trf.ModSpells.SpellInput;
import com.gmail.aamelis.trf.Network.Packets.SpellInputPacket;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class ModClientPayloadHandler {

    public static void sendSpellInput(SpellInput input) {
        ClientPacketDistributor.sendToServer(
                new SpellInputPacket(input)
        );
    }

}
