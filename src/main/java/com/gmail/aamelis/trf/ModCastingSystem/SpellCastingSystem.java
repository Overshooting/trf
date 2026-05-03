package com.gmail.aamelis.trf.ModCastingSystem;

import com.gmail.aamelis.trf.ModPlayerData.PlayerComboManager;
import com.gmail.aamelis.trf.ModPlayerData.PlayerMana;
import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
import com.gmail.aamelis.trf.ModCastingSystem.Combo.ComboBuffer;
import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.Network.Packets.ComboFeedbackPacket;
import com.gmail.aamelis.trf.Network.Packets.CooldownSyncPacket;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf.Registries.ItemsInit;
import com.gmail.aamelis.trf.Registries.SpellsInit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.*;

public class SpellCastingSystem {

    private static final Map<UUID, Map<String, Long>> spellCooldowns = new HashMap<>();
    private static final Map<Short, List<Item>> CLASS_ITEMS = new HashMap<>();

    public static void handleInput(ServerPlayer player, SpellInput input) {

        ComboBuffer buffer = PlayerComboManager.getBuffer(player);

        buffer.addInput(input);

        if (!buffer.isFull()) {
            sendComboUpdate(player, buffer, false, null);
            return;
        }

        SpellInput[] inputs = buffer.getInputs();
        PlayerSpellData spellData = player.getData(AttachmentTypesInit.PLAYER_SPELL_DATA);

        ISpell spell = SpellsInit.get(spellData.getPlayerClass(), inputs[0], inputs[1], inputs[2]);

        boolean success = false;
        if (spell != null) {
            success = attemptCast(player, spell);
        }

        sendComboUpdate(player, buffer, true, success);

        buffer.clear();
    }

    private static boolean attemptCast(ServerPlayer player, ISpell spell) {
        boolean success = false;

        PlayerSpellData playerSpellData = player.getData(AttachmentTypesInit.PLAYER_SPELL_DATA.get());
        PlayerMana playerManaData = player.getData(AttachmentTypesInit.PLAYER_MANA.get());
        ItemStack heldItem = player.getMainHandItem();
        List<Item> validClassItems = CLASS_ITEMS.getOrDefault(spell.getRequiredClass(), Collections.emptyList());

        if (playerSpellData.hasSpell(spell.getId()) && playerManaData.getCurrentMana() >= spell.getRequiredMana() && !isOnCooldown(player, spell) && validClassItems.contains(heldItem.getItem().asItem())) {
            playerManaData.useMana(player, spell.getRequiredMana());
            spell.cast(player);
            setCooldown(player, spell);
            success = true;
        }

        return success;
    }

    private static boolean isOnCooldown(ServerPlayer player, ISpell spell) {
        UUID id = player.getUUID();

        if (!spellCooldowns.containsKey(id)) return false;

        long endTime = spellCooldowns.get(id).getOrDefault(spell.getId(), 0L);

        return System.currentTimeMillis() < endTime;
    }

    private static void setCooldown(ServerPlayer player, ISpell spell) {
        UUID id = player.getUUID();

        spellCooldowns.computeIfAbsent(id, k -> new HashMap<>()).put(spell.getId(), System.currentTimeMillis() + spell.getCooldown());

        sendCooldowns(player);
    }

    public static void sendCooldowns(ServerPlayer player) {
        UUID id = player.getUUID();

        Map<String, Long> map = spellCooldowns.getOrDefault(id, Collections.emptyMap());

        ArrayList<String> ids = new ArrayList<>();
        ArrayList<Long> times = new ArrayList<>();

        for (var entry : map.entrySet()) {
            ids.add(entry.getKey());
            times.add(entry.getValue());
        }

        PacketDistributor.sendToPlayer(player, new CooldownSyncPacket(ids, times));
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

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ComboBuffer buffer = PlayerComboManager.getBuffer(player);

        if (buffer == null || buffer.getSize() == 0) return;

        long now = System.currentTimeMillis();

        if (now - buffer.getLastInputTime() > 500) {
            buffer.clear();
            sendClearPacket(player);
        }

        UUID id = player.getUUID();

        if (spellCooldowns.containsKey(id)) {
            Map<String, Long> cooldowns = spellCooldowns.get(id);

            cooldowns.entrySet().removeIf(entry -> now - entry.getValue() > 60000);

            if (cooldowns.isEmpty()) {
                spellCooldowns.remove(id);
            }
        }
    }

    public static void populateClassItems() {
        CLASS_ITEMS.put(
                PlayerSpellData.MAGE,
                List.of(
                        ItemsInit.BASIC_STAFF_ITEM.get()
                )
        );
    }

}
