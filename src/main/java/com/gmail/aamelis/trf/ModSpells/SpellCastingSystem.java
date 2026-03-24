package com.gmail.aamelis.trf.ModSpells;

import com.gmail.aamelis.trf.ModAttachments.PlayerComboManager;
import com.gmail.aamelis.trf.ModAttachments.PlayerMana;
import com.gmail.aamelis.trf.ModAttachments.PlayerSpellData;
import com.gmail.aamelis.trf.ModComboSystem.ComboBuffer;
import com.gmail.aamelis.trf.Network.ComboFeedbackPacket;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf.Registries.SpellsInit;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;

public class SpellCastingSystem {

    private static final Map<UUID, Long> lastCast = new HashMap<>();

    public static void handleInput(ServerPlayer player, SpellInput input) {

        ComboBuffer buffer = PlayerComboManager.getBuffer(player);

        buffer.addInput(input);

        if (!buffer.isFull()) {
            sendComboUpdate(player, buffer, false, null);
            return;
        }

        SpellInput[] inputs = buffer.getInputs();

        ISpell spell = SpellsInit.get(inputs[0], inputs[1], inputs[2]);

        boolean success = attemptCast(player, spell);

        sendComboUpdate(player, buffer, true, success);

        buffer.clear();
    }

    private static boolean attemptCast(ServerPlayer player, ISpell spell) {
        boolean success = false;

        PlayerSpellData playerSpellData = player.getData(AttachmentTypesInit.PLAYER_SPELL_DATA.get());
        PlayerMana playerManaData = player.getData(AttachmentTypesInit.PLAYER_MANA.get());

        if (spell != null && spell.getRequiredClass().equals(playerSpellData.getPlayerClass()) && playerSpellData.hasSpell(spell.getId()) && playerManaData.getCurrentMana() >= spell.getRequiredMana()) {
            long now = System.currentTimeMillis();

            if (now - lastCast.getOrDefault(player.getUUID(), 0L) >= 200) {
                lastCast.put(player.getUUID(), now);
                playerManaData.useMana(player, spell.getRequiredMana());
                spell.cast(player);
                success = true;
            }
        }

        return success;
    }

    private static void sendComboUpdate(ServerPlayer player, ComboBuffer buffer, boolean finished, Boolean success) {

        ArrayList<Integer> inputs = new ArrayList<>();

        for (int i = 0; i < buffer.getSize(); i++) {
            inputs.add(buffer.getInputs()[i].ordinal());
        }

        PacketDistributor.sendToPlayer(player,
                new ComboFeedbackPacket(inputs, finished, success != null && success)
        );
    }

    private static void sendClearPacket(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player,
                new ComboFeedbackPacket(new ArrayList<>(), false, false));
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ComboBuffer buffer = PlayerComboManager.getBuffer(player);

        if (buffer == null || buffer.getSize() == 0) return;

        long now = System.currentTimeMillis();

        if (now - buffer.getLastInputTime() > 500) {
            buffer.clear();
            sendClearPacket(player);
        }
    }

}
