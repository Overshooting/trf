package com.gmail.aamelis.trf_gi.ModRendering;

import com.gmail.aamelis.trf_gi.ModAttachments.PlayerMana;
import com.gmail.aamelis.trf_gi.Registries.AttachmentTypesInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class ManaBarRenderer {

    public static void renderManaBar(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null || !player.level().isClientSide()) return;

        PlayerMana manaData = player.getData(AttachmentTypesInit.PLAYER_MANA.get());
        GuiGraphics graphics = event.getGuiGraphics();

        int x = mc.getWindow().getGuiScaledWidth() / 2 + 150;
        int y = mc.getWindow().getGuiScaledHeight() - 20;
        String msg = "Mana: " + manaData.getCurrentMana() + "/" + manaData.getMaxMana();
        int width = mc.font.width(msg);
        int maxBars = 10;

        int filledBars = (int) Math.floor((double) manaData.getCurrentMana() / manaData.getMaxMana() * maxBars);

        StringBuilder manaBar = new StringBuilder("[");

        manaBar.append("█".repeat(Math.max(0, filledBars)));

        manaBar.append("░".repeat(Math.max(0, maxBars - filledBars)));

        manaBar.append("]");

        int barWidth = mc.font.width(manaBar.toString());

        graphics.drawString(mc.font, msg, x - width / 2, y - 5, 0xFFFFFFFF,true);
        graphics.drawString(mc.font, manaBar.toString(), x - barWidth / 2, y + 5, 0xFFFFFFFF, true);
    }

}
