package com.gmail.aamelis.trf_gi.Network;

import com.gmail.aamelis.trf_gi.ModSpells.SpellInput;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class ModClientPayloadHandler {

    public static void sendSpellInput(SpellInput input) {
        ClientPacketDistributor.sendToServer(
                new SpellInputPacket(input)
        );
    }

}
