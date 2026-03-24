package com.gmail.aamelis.trf_gi.ModRendering;

import com.gmail.aamelis.trf_gi.ModAttachments.PlayerMana;
import com.gmail.aamelis.trf_gi.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf_gi.TRFGearAndItemsFinalRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class ManaBarRenderer {

    public static final ResourceLocation MANA_BAR_FULL =
            ResourceLocation.fromNamespaceAndPath(TRFGearAndItemsFinalRegistry.MODID, "textures/gui/mana_bar/mana_full.png");

    public static final ResourceLocation MANA_BAR_EMPTY =
            ResourceLocation.fromNamespaceAndPath(TRFGearAndItemsFinalRegistry.MODID, "textures/gui/mana_bar/mana_empty.png");

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
        int relativeMana = (int)(manaData.getCurrentMana() / (double)manaData.getMaxMana() * 64);

        graphics.drawString(mc.font, msg, x - width / 2, y - 7, 0xFFFFFFFF,true);

        graphics.blit(RenderPipelines.GUI_TEXTURED, MANA_BAR_EMPTY, x - 32, y + 3, 0, 0, 64, 16, 64, 16);
        graphics.blit(RenderPipelines.GUI_TEXTURED, MANA_BAR_FULL, x - 32, y + 3, 0, 0, relativeMana, 16, 64, 16);
    }

}
