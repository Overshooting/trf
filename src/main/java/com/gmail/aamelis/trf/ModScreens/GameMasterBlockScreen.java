package com.gmail.aamelis.trf.ModScreens;

import com.gmail.aamelis.trf.ModBlocks.ModBlockEntities.GameMasterBlockEntity;
import com.gmail.aamelis.trf.ModBlocks.ModBlockEntities.GameTypes;
import com.gmail.aamelis.trf.Network.Packets.BackButtonPacket;
import com.gmail.aamelis.trf.Network.Packets.OpenLightsOutMenuPacket;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class GameMasterBlockScreen extends AbstractContainerScreen<GameMasterBlockMenu> {

    public static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            TRFFinalRegistry.MODID, "textures/gui/game_master_block/main_screen.png"
    );

    private Button lightsOutButton, backButton;

    public GameMasterBlockScreen(GameMasterBlockMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.imageWidth = 176;
        this.imageHeight = 166;
        this.titleLabelY = 45;
        this.titleLabelX = 170;
        this.inventoryLabelX = titleLabelX;
        this.inventoryLabelY = titleLabelY + 50;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = leftPos + imageWidth / 2;
        int startY = topPos + 40;

        lightsOutButton = Button.builder(Component.literal("Lights Out"), btn -> {
            if (minecraft != null && minecraft.player != null) {
                ClientPacketDistributor.sendToServer(new OpenLightsOutMenuPacket(menu.getBlockEntity().getBlockPos()));
            }
        })
                .bounds(centerX - 50, startY - 15, 100, 20)
                .build();

        backButton = Button.builder(Component.literal("Home"), btn -> {
            if (minecraft != null && minecraft.player != null) {
                ClientPacketDistributor.sendToServer(new BackButtonPacket(menu.getBlockEntity().getBlockPos()));
            }
        })
                .bounds(centerX + 30, startY - 35, 50, 20)
                .build();

        addRenderableWidget(lightsOutButton);
        addRenderableWidget(backButton);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight, 256, 256);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        this.renderTooltip(guiGraphics, mouseX, mouseY);

        this.renderCustomLabels(guiGraphics);
    }

    private void renderCustomLabels(GuiGraphics guiGraphics) {
        GameMasterBlockEntity blockEntity = menu.getBlockEntity();

        if (blockEntity == null) return;

        GameTypes game = blockEntity.getGame();
        BlockPos[] corners = blockEntity.getCorners();

        if (game == GameTypes.NONE) {
            guiGraphics.drawString(font, "Select a Game:", titleLabelX + 10,  titleLabelY, -12566464, false);

        } else if (game == GameTypes.LIGHTS_OUT) {
            guiGraphics.drawString(font, "Lights Out Controls", titleLabelX - 35, titleLabelY, -12566464, false);

            String corner1String, corner2String;

            if (corners != null) {
                corner1String = corners[0].toString();
                corner2String = corners[1].toString();
            } else {
                corner1String = "None";
                corner2String = "None";
            }

            guiGraphics.drawString(font, "First Corner: " + corner1String, inventoryLabelX - 35, inventoryLabelY - 10, -12566464, false);
            guiGraphics.drawString(font, "Second Corner: " + corner2String, inventoryLabelX - 35, inventoryLabelY, -12566464, false);

            guiGraphics.drawString(font,
                    "Started: " + blockEntity.isStarted(),
                    inventoryLabelX - 35, inventoryLabelY + 20, -12566464, false);

            guiGraphics.drawString(font,
                    "Solved: " + blockEntity.isSolved(),
                    inventoryLabelX - 35, inventoryLabelY + 30, -12566464, false);

            String message = blockEntity.getMessage();
            if (!message.isEmpty()) {
                guiGraphics.drawString(font, message, inventoryLabelX - 35, inventoryLabelY + 45, -12566464, false);
            } else {
                guiGraphics.drawString(font, "Placeholder", inventoryLabelX - 35, inventoryLabelY + 45, -12566464, false);
            }
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();

        GameMasterBlockEntity blockEntity = menu.getBlockEntity();
        if (blockEntity == null) return;

        switch (blockEntity.getGame()) {
            case LIGHTS_OUT -> {
                lightsOutButton.active = false;
                lightsOutButton.visible = false;

                backButton.active = true;
                backButton.visible = true;
            }

            default -> {
                lightsOutButton.active = true;
                lightsOutButton.visible = true;

                backButton.active = false;
                backButton.visible = false;
            }
        }

    }
}
