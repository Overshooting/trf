package com.gmail.aamelis.trf_gi.ModRendering;

import com.gmail.aamelis.trf_gi.ModComboSystem.ClientComboState;
import com.gmail.aamelis.trf_gi.ModSpells.SpellInput;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

import java.util.ArrayList;

public class SpellCastingUIRenderer {

    public static void renderMessage(RenderGuiEvent.Post event) {
        if (System.currentTimeMillis() - ClientComboState.lastUpdateTime > 1000) {
            return;
        }

        var gui = event.getGuiGraphics();
        var mc = Minecraft.getInstance();

        int x = mc.getWindow().getGuiScaledWidth() / 2;
        int y = mc.getWindow().getGuiScaledHeight() - 60;

        if (!ClientComboState.resultMessage.isEmpty()) {
            if (System.currentTimeMillis() - ClientComboState.resultTime < 400) {
                int color = ClientComboState.resultMessage.equals("CAST SUCCESS") ? 0xFF00FF00 : 0xFFFF0000;

                String result = ClientComboState.resultMessage;
                int resultWidth = mc.font.width(result);

                gui.drawString(mc.font, result, x - resultWidth / 2, y, color, true);
                ClientComboState.currentInputs.clear();
                return;
            }
        }

        if (ClientComboState.currentInputs.isEmpty()) return;

        StringBuilder message = new StringBuilder();

        for (SpellInput input : ClientComboState.currentInputs) {
            message.append("- ").append(input.name()).append(" ");
        }

        message.append("-");

        String msg = message.toString();
        int width = mc.font.width(msg);

        gui.drawString(mc.font, msg, x - width / 2, y, 0xFFFFFFFF, true);
    }

}
