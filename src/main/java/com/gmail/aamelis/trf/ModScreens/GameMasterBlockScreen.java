package com.gmail.aamelis.trf.ModScreens;

import com.gmail.aamelis.trf.ModBlocks.ModBlockEntities.GameMasterBlockEntity;
import com.gmail.aamelis.trf.ModBlocks.ModBlockEntities.GameTypes;
import com.gmail.aamelis.trf.Network.Packets.BackButtonPacket;
import com.gmail.aamelis.trf.Network.Packets.OpenLightsOutMenuPacket;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
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
                .bounds(centerX - 50, startY, 100, 20)
                .build();

        backButton = Button.builder(Component.literal("Home"), btn -> {
            if (minecraft != null && minecraft.player != null) {
                ClientPacketDistributor.sendToServer(new BackButtonPacket(menu.getBlockEntity().getBlockPos()));
            }
        })
                .bounds(centerX - 75, startY - 30, 50, 20)
                .build();

        addRenderableWidget(lightsOutButton);
        addRenderableWidget(backButton);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        GameTypes game = menu.getBlockEntity().getGame();
        GameMasterBlockEntity blockEntity = menu.getBlockEntity();

        if (blockEntity == null) return;

        guiGraphics.drawString(font, title, leftPos + 8, topPos + 6, 0x404040, false);

        if (game == GameTypes.NONE) {
            guiGraphics.drawString(font, "Select a Game:", leftPos + 8, topPos + 25, 0xFFFFFF, false);

        } else if (game == GameTypes.LIGHTS_OUT) {
            guiGraphics.drawString(font, "Lights Out Controls", leftPos + 8, topPos + 25, 0xFFFFFF, false);

            guiGraphics.drawString(font,
                    "Started: " + blockEntity.isStarted(),
                    leftPos + 8, topPos + 45, 0xAAAAAA, false);

            guiGraphics.drawString(font,
                    "Solved: " + blockEntity.isSolved(),
                    leftPos + 8, topPos + 60, 0xAAAAAA, false);

            String message = blockEntity.getMessage();
            if (!message.isEmpty()) {
                guiGraphics.drawString(font, message, leftPos + 8, topPos + imageHeight - 20, 0xFF5555, false);
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
