package com.gmail.aamelis.trf.ModScreens;

import ca.weblite.objc.Client;
import com.gmail.aamelis.trf.ModBlocks.ModBlockEntities.GameMasterBlockEntity;
import com.gmail.aamelis.trf.ModBlocks.ModBlockEntities.GameTypes;
import com.gmail.aamelis.trf.Network.Packets.BackButtonPacket;
import com.gmail.aamelis.trf.Network.Packets.OpenLightsOutMenuPacket;
import com.gmail.aamelis.trf.Network.Packets.SetCornersPacket;
import com.gmail.aamelis.trf.Network.Packets.SetMessagePacket;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.List;

public class GameMasterBlockScreen extends AbstractContainerScreen<GameMasterBlockMenu> {

    public static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            TRFFinalRegistry.MODID, "textures/gui/game_master_block/main_screen.png"
    );

    private EditBox corner1X, corner1Y, corner1Z, corner2X, corner2Y, corner2Z;
    private Button lightsOutButton, backButton, sendCornersButton;
    private String message;

    public GameMasterBlockScreen(GameMasterBlockMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        message = "";

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

        sendCornersButton = Button.builder(Component.literal("Set Corners"), btn -> {
            try {
                int c1x = Integer.parseInt(corner1X.getValue());
                int c1y = Integer.parseInt(corner1Y.getValue());
                int c1z = Integer.parseInt(corner1Z.getValue());

                int c2x = Integer.parseInt(corner2X.getValue());
                int c2y = Integer.parseInt(corner2Y.getValue());
                int c2z = Integer.parseInt(corner2Z.getValue());

                if (c1y != c2y) {
                    ClientPacketDistributor.sendToServer(new SetMessagePacket(menu.getBlockEntity().getBlockPos(), "Y coordinates must be the same!"));
                    return;
                }

                BlockPos pos1 = new BlockPos(c1x, c1y, c1z);
                BlockPos pos2 = new BlockPos(c2x, c2y, c2z);

                ClientPacketDistributor.sendToServer(new SetCornersPacket(menu.getBlockEntity().getBlockPos(), pos1, pos2));

                corner1X.setValue("");
                corner1Y.setValue("");
                corner1Z.setValue("");
                corner2X.setValue("");
                corner2Y.setValue("");
                corner2Z.setValue("");
            } catch (NumberFormatException e) {
                ClientPacketDistributor.sendToServer(new SetMessagePacket(menu.getBlockEntity().getBlockPos(), "Invalid input: all fields must be numbers"));
            }
        })
                .bounds(centerX + 20, startY + 70, 60, 20)
                .build();

        corner1X = new EditBox(font, centerX - 80, startY + 70, 30, 20, Component.literal("C1X"));
        corner1Y = new EditBox(font, centerX - 50, startY + 70, 30, 20, Component.literal("C1Y"));
        corner1Z = new EditBox(font, centerX - 20, startY + 70, 30, 20, Component.literal("C1Z"));

        corner2X = new EditBox(font, centerX - 80, startY + 95, 30, 20, Component.literal("C2X"));
        corner2Y = new EditBox(font, centerX - 50, startY + 95, 30, 20, Component.literal("C2Y"));
        corner2Z = new EditBox(font, centerX - 20, startY + 95, 30, 20, Component.literal("C2Z"));

        addRenderableWidget(corner1X);
        addRenderableWidget(corner1Y);
        addRenderableWidget(corner1Z);
        addRenderableWidget(corner2X);
        addRenderableWidget(corner2Y);
        addRenderableWidget(corner2Z);
        addRenderableWidget(lightsOutButton);
        addRenderableWidget(backButton);
        addRenderableWidget(sendCornersButton);
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
                corner1String = corners[0].getX() + ", " + corners[0].getY() + ", " + corners[0].getZ();
                corner2String = corners[1].getX() + ", " + corners[1].getY() + ", " + corners[1].getZ();
            } else {
                corner1String = "None";
                corner2String = "None";
            }

            guiGraphics.drawString(font, "First Corner: " + corner1String, inventoryLabelX - 35, inventoryLabelY - 30, -12566464, false);
            guiGraphics.drawString(font, "Second Corner: " + corner2String, inventoryLabelX - 35, inventoryLabelY - 20, -12566464, false);

            guiGraphics.drawString(font,
                    "Started: " + blockEntity.isStarted(),
                    inventoryLabelX - 35, inventoryLabelY, -12566464, false);

            guiGraphics.drawString(font,
                    "Solved: " + blockEntity.isSolved(),
                    inventoryLabelX - 35, inventoryLabelY + 10, -12566464, false);

            if (!message.isEmpty()) {
                int maxWidth = 150;
                int lineHeight = 10;
                int x = inventoryLabelX - 35;
                int y = inventoryLabelY + 25;

                List<FormattedCharSequence> lines = font.split(Component.literal(message), maxWidth);
                for (int i = 0; i < lines.size(); i++) {
                    guiGraphics.drawString(font, lines.get(i), x, y + i * lineHeight, -12566464, false);
                }
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
                setUpLightsOutScreen(blockEntity);
            }

            default -> {
                setUpHomeScreen();
            }
        }

    }

    private void setUpLightsOutScreen(GameMasterBlockEntity blockEntity) {
        this.lightsOutButton.active = false;
        this.lightsOutButton.visible = false;

        backButton.active = true;
        backButton.visible = true;

        sendCornersButton.active = true;
        sendCornersButton.visible = true;

        corner1X.active = true;
        corner1Y.active = true;
        corner1Z.active = true;
        corner2X.active = true;
        corner2Y.active = true;
        corner2Z.active = true;

        corner1X.visible = true;
        corner1Y.visible = true;
        corner1Z.visible = true;
        corner2X.visible = true;
        corner2Y.visible = true;
        corner2Z.visible = true;

        message = blockEntity.getMessage();
    }

    private void setUpHomeScreen() {
        this.lightsOutButton.active = true;
        this.lightsOutButton.visible = true;

        backButton.active = false;
        backButton.visible = false;

        sendCornersButton.active = false;
        sendCornersButton.visible = false;

        corner1X.active = false;
        corner1Y.active = false;
        corner1Z.active = false;
        corner2X.active = false;
        corner2Y.active = false;
        corner2Z.active = false;

        corner1X.visible = false;
        corner1Y.visible = false;
        corner1Z.visible = false;
        corner2X.visible = false;
        corner2Y.visible = false;
        corner2Z.visible = false;
    }
}
