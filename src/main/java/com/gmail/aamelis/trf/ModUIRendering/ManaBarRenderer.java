package com.gmail.aamelis.trf.ModUIRendering;

import com.gmail.aamelis.trf.ModAttachments.PlayerMana;
import com.gmail.aamelis.trf.ModAttachments.PlayerSpellData;
import com.gmail.aamelis.trf.ModSpells.CastingSystem.ClientCooldownState;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf.Registries.SpellsInit;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.Map;

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

        int manaTextY = y - 15;
        graphics.drawString(mc.font, msg, x - width / 2, manaTextY, 0xFFFFFFFF, true);

        graphics.pose().pushMatrix();

        graphics.pose().scale(1.0f, 0.75f);

        graphics.blit(RenderPipelines.GUI_TEXTURED, realEmptyPath, x - 50, y + 40, 0, 0, 100, 64, 100, 64);
        graphics.blit(RenderPipelines.GUI_TEXTURED, realFullPath, x - 50, y + 40, 0, 0, relativeMana, 64, 100, 64);

        graphics.pose().popMatrix();
        renderCooldowns(graphics, mc, x, manaTextY);
    }

    private static void renderCooldowns(GuiGraphics gui, Minecraft mc, int baseX, int manaTextY) {
        if (ClientCooldownState.cooldowns.isEmpty()) return;

        int startY = manaTextY - 20;

        long now = System.currentTimeMillis();
        int index = 0;

        int iconSize = 15;
        int spacing = 5;
        int iconsPerRow = 5;

        var sorted = ClientCooldownState.cooldowns.entrySet().stream().sorted(Map.Entry.comparingByValue()).toList();

        for (var entry : sorted) {
            String spellId = entry.getKey();
            long endTime = entry.getValue();

            long remaining = endTime - now;
            if (remaining <= 0) continue;

            ISpell spell = SpellsInit.get(spellId);
            if (spell == null) throw new IllegalStateException("Unregistered spell: " + spellId + " found in player data");

            float progress = Math.min(1.0f, 1.0f - (remaining / (float)spell.getCooldown()));

            int col = index % iconsPerRow;
            int row = index / iconsPerRow;

            int totalRowWidth = iconsPerRow * (iconSize + spacing) - spacing;
            int startX = baseX - totalRowWidth / 2;

            int x = startX + col * (iconSize + spacing);
            int y = startY - row * (iconSize + spacing);

            gui.blit(RenderPipelines.GUI_TEXTURED, spell.getEmptyPath(), x, y, 0, 0, iconSize, iconSize, iconSize, iconSize);

            int filledHeight = (int)(iconSize * progress);
            int fillY = y + (iconSize - filledHeight);

            gui.blit(RenderPipelines.GUI_TEXTURED, spell.getFullPath(), x, fillY, 0, iconSize - filledHeight, iconSize, filledHeight, iconSize, iconSize);

            index++;
        }
    }

}
