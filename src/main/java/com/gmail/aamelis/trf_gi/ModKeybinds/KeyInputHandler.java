package com.gmail.aamelis.trf_gi.ModKeybinds;

import com.gmail.aamelis.trf_gi.ModSpells.SpellInput;
import com.gmail.aamelis.trf_gi.Network.ModClientPayloadHandler;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.ClientTickEvent;

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
        ModClientPayloadHandler.sendSpellInput(input);
    }



}
