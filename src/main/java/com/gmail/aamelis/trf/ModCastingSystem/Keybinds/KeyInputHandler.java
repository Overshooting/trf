package com.gmail.aamelis.trf.ModCastingSystem.Keybinds;

import com.gmail.aamelis.trf.Network.Packets.SpellInputPacket;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class KeyInputHandler {

    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) return;

        while (CastKeybinds.SPELL_C.consumeClick()) {
            sendInput(SpellInput.C);
        }

        while (CastKeybinds.SPELL_V.consumeClick()) {
            sendInput(SpellInput.V);
        }

        while (CastKeybinds.SPELL_B.consumeClick()) {
            sendInput(SpellInput.B);
        }
    }

    private static void sendInput(SpellInput input) {
        ClientPacketDistributor.sendToServer(
                new SpellInputPacket(input)
        );
    }



}
