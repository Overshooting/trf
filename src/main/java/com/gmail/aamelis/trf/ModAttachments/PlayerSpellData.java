package com.gmail.aamelis.trf.ModAttachments;

import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerSpellData {

    public static final short EMPTY = 0;
    public static final short WARRIOR = 1;
    public static final short MAGE = 2;
    public static final short ARCHER = 3;
    public static final short CLERIC = 4;


    private short playerClass;
    private final Set<String> unlockedSpells;

    public PlayerSpellData() {
        this(EMPTY, new HashSet<>());
    }

    public PlayerSpellData(short playerClass, Set<String> unlockedSpells) {
        this.playerClass = playerClass;
        this.unlockedSpells = new HashSet<>(unlockedSpells);
    }

    public PlayerSpellData(short s, List<String> strings) {
        this.playerClass = s;
        this.unlockedSpells = new HashSet<>(strings);
    }

    public short getPlayerClass() {
        return playerClass;
    }

    public String getPlayerClassString() {
        String realClass = "None";

        switch (playerClass) {
            case 1 -> realClass = "Warrior";
            case 2 -> realClass = "Mage";
            case 3 -> realClass = "Archer";
            case 4 -> realClass = "Cleric";
        }

        return realClass;
    }

    public void setPlayerClass(short newClass, ServerPlayer player) {
        playerClass = newClass;

        setDirty(player);
    }

    public void unlockSpell(String spellId, ServerPlayer player) {
        unlockedSpells.add(spellId);

        setDirty(player);
    }

    public boolean hasSpell(String spellId) {
        return unlockedSpells.contains(spellId);
    }

    public Collection<String> getUnlockedSpells() {
        return unlockedSpells;
    }

    private void setDirty(ServerPlayer player) {
        player.setData(AttachmentTypesInit.PLAYER_SPELL_DATA, new PlayerSpellData(playerClass, unlockedSpells));
    }

    public static List<String> getAllClassStrings() {
        return List.of("Default", "Warrior", "Mage", "Archer", "Cleric");
    }

    public static final MapCodec<PlayerSpellData> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            Codec.SHORT.fieldOf("player_class")
                                    .forGetter(PlayerSpellData::getPlayerClass),
                            Codec.STRING.listOf().fieldOf("spells")
                                    .forGetter(data -> List.copyOf(data.unlockedSpells))
                    ).apply(instance, PlayerSpellData::new)
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerSpellData> STREAM_CODEC =
            StreamCodec.of(
                    (buf, playerSpellData) -> {
                        buf.writeShort(playerSpellData.getPlayerClass());
                        buf.writeInt(playerSpellData.getUnlockedSpells().size());
                        for (String spellName : playerSpellData.getUnlockedSpells()) {
                            buf.writeUtf(spellName);
                        }
                    },
                    (buf) -> {
                        short playerClass = buf.readShort();

                        int size = buf.readInt();
                        Set<String> spells = new HashSet<>();
                        for (int i = 0; i < size; i++) {
                            spells.add(buf.readUtf());
                        }

                        return new PlayerSpellData(playerClass, spells);
                    }

            );

}
