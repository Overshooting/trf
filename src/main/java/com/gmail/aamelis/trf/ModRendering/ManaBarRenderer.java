package com.gmail.aamelis.trf.ModRendering;

import com.gmail.aamelis.trf.ModAttachments.PlayerMana;
import com.gmail.aamelis.trf.ModAttachments.PlayerSpellData;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class ManaBarRenderer {

    public static void renderManaBar(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null || !player.level().isClientSide()) return;

        PlayerMana manaData = player.getData(AttachmentTypesInit.PLAYER_MANA.get());
        PlayerSpellData spellData = player.getData(AttachmentTypesInit.PLAYER_SPELL_DATA.get());
        GuiGraphics graphics = event.getGuiGraphics();

        if (spellData.getPlayerClass() == 0) {
            return;
        }

        int x = mc.getWindow().getGuiScaledWidth() / 2 + 150;
        int y = mc.getWindow().getGuiScaledHeight() - 20;
        String msg = "Mana: " + manaData.getCurrentMana() + "/" + manaData.getMaxMana();
        int width = mc.font.width(msg);
        int relativeMana = (int) (manaData.getCurrentMana() / (double) manaData.getMaxMana() * 100);
        String className = spellData.getPlayerClassString().toLowerCase();

        ResourceLocation realEmptyPath = ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/mana_bars/" + className + "_mana_bar_empty.png");
        ResourceLocation realFullPath = ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/mana_bars/" + className + "_mana_bar_full.png");

        graphics.drawString(mc.font, msg, x - width / 2, y - 10, 0xFFFFFFFF, true);

        graphics.pose().pushMatrix();

        graphics.pose().scale(1.0f, 0.75f);

        graphics.blit(RenderPipelines.GUI_TEXTURED, realEmptyPath, x - 50, y + 40, 0, 0, 100, 64, 100, 64);
        graphics.blit(RenderPipelines.GUI_TEXTURED, realFullPath, x - 50, y + 40, 0, 0, relativeMana, 64, 100, 64);

        graphics.pose().popMatrix();
    }

}
