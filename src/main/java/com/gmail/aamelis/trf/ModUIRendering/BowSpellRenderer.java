package com.gmail.aamelis.trf.ModUIRendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class BowSpellRenderer {

    private static int color = 0xFFFFFFFF;
    private static long startTime = 0, endTime = 0;

    public static void renderTimer(RenderGuiEvent.Post event) {
        if (endTime <= 0 || startTime < 0) return;
        Minecraft mc = Minecraft.getInstance();
        GuiGraphics graphics = event.getGuiGraphics();

        int screenW = mc.getWindow().getGuiScaledWidth();
        int screenH = mc.getWindow().getGuiScaledHeight();

        int centerX = screenW / 2;
        int centerY = screenH / 2;

        int barWidth = 60;
        int barHeight = 4;
        int yOffset = 6;

        int x1 = centerX - barWidth / 2;
        int y1 = centerY + yOffset;
        int x2 = centerX + barWidth / 2;
        int y2 = y1 + barHeight;

        long now = System.currentTimeMillis();
        long totalDuration = endTime - startTime;
        long remainingDuration = endTime - now;

        if (remainingDuration <= 0) {
            endTime = 0;
            return;
        }

        float progress = (float) remainingDuration / totalDuration;

        graphics.fill(x1, y1, x2, y2, 0x88000000);

        int filledWidth = (int)(barWidth * progress);
        graphics.fill(x1, y1, x1 + filledWidth, y2, color);
    }

    public static void renewTimer(long thisEndTime, int thisColor) {
        endTime = thisEndTime;
        startTime = System.currentTimeMillis();
        color = thisColor;
    }

}
