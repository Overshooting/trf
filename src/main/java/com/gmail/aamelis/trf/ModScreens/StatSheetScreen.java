package com.gmail.aamelis.trf.ModScreens;

import com.gmail.aamelis.trf.ModPlayerData.ModStats.Levels.PlayerLevelData;
import com.gmail.aamelis.trf.ModPlayerData.ModStats.PlayerStatData;
import com.gmail.aamelis.trf.Network.Packets.StatIncreasePacket;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StatSheetScreen extends Screen {
    private static final ResourceLocation BACKGROUND_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/stat_sheet/background.png");

    private final List<Button> statButtons = new ArrayList<>();

    private final Player player;

    private final int imageWidth = 194;
    private final int imageHeight = 159;

    private int leftPos;
    private int topPos;

    private static final int COLOR_LABEL = 0xFF404040;
    private static final int COLOR_VALUE = 0xFF202020;

    public StatSheetScreen(Player player) {
        super(Component.literal("Stats"));
        this.player = player;
    }

    @Override
    protected void init() {
        super.init();

        leftPos = (width - imageWidth) / 2;
        topPos = (height - imageHeight) / 2;

        statButtons.clear();

        int xButton = leftPos + 160;
        int yStart = topPos + 50;
        int lineHeight = 18;

        addStatButton(xButton, yStart, "con");
        addStatButton(xButton, yStart + lineHeight, "man");
        addStatButton(xButton, yStart + lineHeight * 2, "str");
        addStatButton(xButton, yStart + lineHeight * 3, "mag");
        addStatButton(xButton, yStart + lineHeight * 4, "per");
        addStatButton(xButton, yStart + lineHeight * 5, "pie");
    }

    private void addStatButton(int x, int y, String stat) {
        Button button = Button.builder(Component.literal("+"), btn -> {
                    onStatIncrease(stat);
                })
                .bounds(x, y - 2, 12, 12) // small square button
                .build();

        statButtons.add(button);
        addRenderableWidget(button);
    }

    private void onStatIncrease(String stat) {
        PlayerLevelData levelData = player.getData(AttachmentTypesInit.PLAYER_LEVEL);

        if (levelData.getPoints() <= 0) return;

        ClientPacketDistributor.sendToServer(new StatIncreasePacket(stat));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        PlayerLevelData levelData = player.getData(AttachmentTypesInit.PLAYER_LEVEL);

        boolean hasPoints = levelData.getPoints() > 0;

        for (Button button : statButtons) {
            button.visible = hasPoints;
            button.active = hasPoints;
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        renderStats(guiGraphics);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBlurredBackground(guiGraphics);

        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                BACKGROUND_TEXTURE,
                leftPos, topPos,
                0, 0,
                imageWidth, imageHeight,
                imageWidth, imageHeight,
                256, 256
        );
    }

    private void renderStats(GuiGraphics guiGraphics) {
        PlayerStatData stats = player.getData(AttachmentTypesInit.PLAYER_STATS);
        PlayerLevelData levelData = player.getData(AttachmentTypesInit.PLAYER_LEVEL);

        int xLabel = leftPos + 12;
        int xValue = leftPos + 140;


        int yTop = topPos + 8;

        guiGraphics.drawString(font, "Level", xLabel, yTop, COLOR_LABEL, false);
        guiGraphics.drawString(font,
                String.valueOf(levelData.getLevel()),
                xValue, yTop, COLOR_VALUE, false);

        yTop += 12;
        guiGraphics.drawString(font, "XP", xLabel, yTop, COLOR_LABEL, false);
        guiGraphics.drawString(font,
                levelData.getReadableExperience() + " / " + levelData.getReadableMaxExperience(),
                xValue, yTop, COLOR_VALUE, false);

        yTop += 12;
        guiGraphics.drawString(font, "Points", xLabel, yTop, COLOR_LABEL, false);
        guiGraphics.drawString(font,
                String.valueOf(levelData.getPoints()),
                xValue, yTop, COLOR_VALUE, false);

        int y = topPos + 50;
        int lineHeight = 18;

        guiGraphics.drawString(font, "Constitution", xLabel, y, COLOR_LABEL, false);
        guiGraphics.drawString(font,
                String.valueOf(stats.getConstitution()),
                xValue, y, COLOR_VALUE, false);

        guiGraphics.drawString(font, "Mana", xLabel, y += lineHeight, COLOR_LABEL, false);
        guiGraphics.drawString(font,
                String.valueOf(stats.getMana()),
                xValue, y, COLOR_VALUE, false);

        guiGraphics.drawString(font, "Strength", xLabel, y += lineHeight, COLOR_LABEL, false);
        guiGraphics.drawString(font,
                String.valueOf(stats.getStrength()),
                xValue, y, COLOR_VALUE, false);

        guiGraphics.drawString(font, "Magic", xLabel, y += lineHeight, COLOR_LABEL, false);
        guiGraphics.drawString(font,
                String.valueOf(stats.getMagic()),
                xValue, y, COLOR_VALUE, false);

        guiGraphics.drawString(font, "Perception", xLabel, y += lineHeight, COLOR_LABEL, false);
        guiGraphics.drawString(font,
                String.valueOf(stats.getPerception()),
                xValue, y, COLOR_VALUE, false);

        guiGraphics.drawString(font, "Piety", xLabel, y += lineHeight, COLOR_LABEL, false);
        guiGraphics.drawString(font,
                String.valueOf(stats.getPiety()),
                xValue, y, COLOR_VALUE, false);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
