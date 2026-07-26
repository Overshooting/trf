package com.gmail.aamelis.trf.ModScreens;

import com.gmail.aamelis.trf.ModBlocks.ModBlockEntities.GameMasterBlockEntity;
import com.gmail.aamelis.trf.ModBlocks.ModBlockEntities.GameType;
import com.gmail.aamelis.trf.ModScreens.Utils.ScreenContainer;
import com.gmail.aamelis.trf.Network.Packets.*;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameMasterBlockScreen extends AbstractContainerScreen<GameMasterBlockMenu> {

    public static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            TRFFinalRegistry.MODID, "textures/gui/game_master_block/main_screen.png"
    );

    public static final String HOME_ID = "home";
    public static final String LIGHTS_OUT_ID = "lights";

    private EditBox corner1X, corner1Y, corner1Z, corner2X, corner2Y, corner2Z;
    private Button lightsOutButton, backButton, sendCornersButton, startButton, resetButton;
    private String message;
    private final ScreenContainer[] screens = new ScreenContainer[2];

    public GameMasterBlockScreen(GameMasterBlockMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        message = "";

        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();

        int left = leftPos;
        int top = topPos;
        int right = left + imageWidth;
        int bottom = topPos + imageHeight;

        int centerX = left + imageWidth / 2;

        int fieldWidth = 30;

        int inputStartX = left + 10;
        int inputRow1Y = bottom - 50;
        int inputRow2Y = bottom - 25;

        ScreenContainer homeScreen = new ScreenContainer(HOME_ID);
        ScreenContainer lightsOutScreen = new ScreenContainer(LIGHTS_OUT_ID);

        lightsOutButton = Button.builder(Component.literal("Lights Out"), btn -> {
            if (minecraft != null && minecraft.player != null) {
                ClientPacketDistributor.sendToServer(new OpenLightsOutMenuPacket(menu.getBlockEntity().getBlockPos()));
            }
        })
                .bounds(centerX - 50, top + 30, 100, 20)
                .build();

        backButton = Button.builder(Component.literal("Home"), btn -> {
            if (minecraft != null && minecraft.player != null) {
                ClientPacketDistributor.sendToServer(new BackButtonPacket(menu.getBlockEntity().getBlockPos()));
            }
        })
                .bounds(right - 55, top + 5, 50, 20)
                .build();

        sendCornersButton = Button.builder(Component.literal("Set Corners"), btn -> {
            if (minecraft != null && minecraft.player != null) {
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

                    ClientPacketDistributor.sendToServer(new SetMessagePacket(menu.getBlockEntity().getBlockPos(), "Corners set successfully!"));
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
            }
        })
                .bounds(centerX + 20, top + 80, 60, 20)
                .build();

        startButton = Button.builder(Component.literal("Start"), btn -> {
            if (minecraft != null && minecraft.player != null) {
                ClientPacketDistributor.sendToServer(new SetMessagePacket(menu.getBlockEntity().getBlockPos(), "Game Started!"));
                ClientPacketDistributor.sendToServer(new StartGamePacket(menu.getBlockEntity().getBlockPos()));
            }
        })
                .bounds(centerX + 20, top + 105, 60, 20)
                .build();

        resetButton = Button.builder(Component.literal("Reset"), btn -> {
            if (minecraft != null && minecraft.player != null) {
                ClientPacketDistributor.sendToServer(new SetMessagePacket(menu.getBlockEntity().getBlockPos(), "Game Reset!"));
                ClientPacketDistributor.sendToServer(new ResetGamePacket(menu.getBlockEntity().getBlockPos()));
            }
        })
                .bounds(centerX + 20, top + 130, 60, 20)
                .build();

        corner1X = new EditBox(font, inputStartX, inputRow1Y, fieldWidth, 20, Component.literal("C1X"));
        corner1Y = new EditBox(font, inputStartX + fieldWidth, inputRow1Y, fieldWidth, 20, Component.literal("C1Y"));
        corner1Z = new EditBox(font, inputStartX + fieldWidth * 2, inputRow1Y, fieldWidth, 20, Component.literal("C1Z"));

        corner2X = new EditBox(font, inputStartX, inputRow2Y, fieldWidth, 20, Component.literal("C2X"));
        corner2Y = new EditBox(font, inputStartX + fieldWidth, inputRow2Y, fieldWidth, 20, Component.literal("C2Y"));
        corner2Z = new EditBox(font, inputStartX + fieldWidth * 2, inputRow2Y, fieldWidth, 20, Component.literal("C2Z"));

        AbstractWidget[] homeArray = {lightsOutButton};
        List<AbstractWidget> homeWidgets = List.of(homeArray);

        AbstractWidget[] lightsOutArray = {backButton, sendCornersButton, startButton, resetButton, corner1X, corner1Y, corner1Z, corner2X, corner2Y, corner2Z};
        List<AbstractWidget> lightsOutWidgets = List.of(lightsOutArray);

        for (AbstractWidget widget : homeWidgets) {
            addRenderableWidget(widget);
        }

        for (AbstractWidget widget : lightsOutWidgets) {
            addRenderableWidget(widget);
        }

        homeScreen.setWidgets(homeWidgets);
        lightsOutScreen.setWidgets(lightsOutWidgets);

        screens[0] = homeScreen;
        screens[1] = lightsOutScreen;
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

        GameType game = blockEntity.getGame();
        BlockPos[] corners = blockEntity.getCorners();

        int left = leftPos + 10;
        int top = topPos + 10;

        if (game == GameType.NONE) {
            guiGraphics.drawString(font, "Select a Game:", left,  top, -12566464, false);

        } else if (game == GameType.LIGHTS_OUT) {
            guiGraphics.drawString(font, "Lights Out Controls", left, top, -12566464, false);

            String corner1String, corner2String;

            if (corners != null) {
                corner1String = corners[0].getX() + ", " + corners[0].getY() + ", " + corners[0].getZ();
                corner2String = corners[1].getX() + ", " + corners[1].getY() + ", " + corners[1].getZ();
            } else {
                corner1String = "None";
                corner2String = "None";
            }

            guiGraphics.drawString(font, "Corners: ", left, top + 15, -12566464, false);
            guiGraphics.drawString(font, corner1String, left, top + 25, -12566464, false);
            guiGraphics.drawString(font, corner2String, left, top + 35, -12566464, false);

            guiGraphics.drawString(font,
                    "Started: " + blockEntity.isStarted(),
                    left, top + 45, -12566464, false);

            guiGraphics.drawString(font,
                    "Solved: " + blockEntity.isSolved(),
                    left, top + 55, -12566464, false);

            if (!message.isEmpty()) {
                int maxWidth = 100;
                int lineHeight = 10;
                int y = top + 70;

                List<FormattedCharSequence> lines = font.split(Component.literal(message), maxWidth);
                for (int i = 0; i < lines.size(); i++) {
                    guiGraphics.drawString(font, lines.get(i), left, y + i * lineHeight, -12566464, false);
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
                setScreen(LIGHTS_OUT_ID);
            }

            default -> {
                setScreen(HOME_ID);
            }
        }

    }

    private void setScreen(String id) {
        for (ScreenContainer screen : screens) {
            if (screen.getContainerName().equals(id)) {
                for (AbstractWidget widget : screen.getWidgets()) {
                    widget.active = true;
                    widget.visible = true;
                }
            } else {
                for (AbstractWidget widget : screen.getWidgets()) {
                    widget.active = false;
                    widget.visible = false;
                }
            }
        }
    }
}
