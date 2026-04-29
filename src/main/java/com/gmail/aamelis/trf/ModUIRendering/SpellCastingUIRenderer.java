package com.gmail.aamelis.trf.ModUIRendering;

import com.gmail.aamelis.trf.ModCastingSystem.Combo.ClientComboState;
import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class SpellCastingUIRenderer {

    public static void renderMessage(RenderGuiEvent.Post event) {
        var gui = event.getGuiGraphics();
        var mc = Minecraft.getInstance();
        if (System.currentTimeMillis() - ClientComboState.lastUpdateTime < 1000) {
            int x = mc.getWindow().getGuiScaledWidth() / 2;
            int y = mc.getWindow().getGuiScaledHeight() - 50;
            int color = 0xFFFFFFFF;

            if (ClientComboState.resultState != ClientComboState.NOT_FINISHED) {
                if (System.currentTimeMillis() - ClientComboState.resultTime < 1000) {
                    if (ClientComboState.resultState == ClientComboState.SUCCESS) {
                        color = 0xFF00FF00;
                    } else if (ClientComboState.resultState == ClientComboState.FAILURE) {
                        color = 0xFFFF0000;
                    }
                }
            }

            if (ClientComboState.currentInputs.isEmpty()) return;

            StringBuilder message = new StringBuilder();

            for (SpellInput input : ClientComboState.currentInputs) {
                String nameSymbol = parseSymbol(input.name());
                message.append("- ").append(nameSymbol).append(" ");
            }

            message.append("-");

            String msg = message.toString();
            int width = mc.font.width(msg);

            gui.drawString(mc.font, msg, x - width / 2, y, color, true);
        }

    }

    private static String parseSymbol(String str) {
        return switch (str) {
            case "C" -> "ᓵ";
            case "V" -> "⍊";
            case "B" -> "ʖ";
            default -> throw new IllegalStateException("Illegal instance of SpellInput with name " + str + " detected!");
        };
    }

}
