package com.gmail.aamelis.trf.Network;

import com.gmail.aamelis.trf.ModSpellRendering.LightningBeam.ClientLightningBeamManager;
import com.gmail.aamelis.trf.Network.Packets.LightningBeamPacket;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SpellCastingHandler {

    public static void handleLightningBeam(LightningBeamPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientLightningBeamManager.addBeam(payload.start(), payload.end(), payload.impact());
        });
    }

}
