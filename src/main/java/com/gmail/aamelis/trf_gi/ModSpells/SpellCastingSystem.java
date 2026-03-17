package com.gmail.aamelis.trf_gi.ModSpells;

import com.gmail.aamelis.trf_gi.ModAttachments.PlayerComboManager;
import com.gmail.aamelis.trf_gi.ModAttachments.PlayerSpellData;
import com.gmail.aamelis.trf_gi.ModComboSystem.ComboBuffer;
import com.gmail.aamelis.trf_gi.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf_gi.Registries.SpellsInit;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SpellCastingSystem {

    private static final Map<UUID, Long> lastCast = new HashMap<>();

    public static void handleInput(ServerPlayer player, SpellInput input) {

        ComboBuffer buffer = PlayerComboManager.getBuffer(player);

        buffer.addInput(input);

        if (!buffer.isFull()) return;

        List<SpellInput> combo = buffer.getCombo();

        attemptCast(player, combo);

        buffer.clear();
    }

    private static void attemptCast(ServerPlayer player, List<SpellInput> combo) {
        long now = System.currentTimeMillis();

        if (now - lastCast.getOrDefault(player.getUUID(), 0L) < 200) return;

        PlayerSpellData playerSpellData = player.getData(AttachmentTypesInit.PLAYER_SPELL_DATA.get());

        for (ISpell spell : SpellsInit.getAllSpells()) {
            if (spell.getCombo().equals(combo) && spell.getRequiredClass().equals(playerSpellData.getPlayerClass()) && playerSpellData.hasSpell(spell.getId())) {
                spell.cast(player);
                return;
            }
        }
    }

}
