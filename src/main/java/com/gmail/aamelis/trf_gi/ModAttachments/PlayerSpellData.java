package com.gmail.aamelis.trf_gi.ModAttachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerSpellData {

    private String playerClass;
    private final Set<String> unlockedSpells;

    public PlayerSpellData() {
        this("default", new HashSet<>());
    }

    public PlayerSpellData(String playerClass, Set<String> unlockedSpells) {
        this.playerClass = playerClass;
        this.unlockedSpells = new HashSet<>(unlockedSpells);
    }

    public PlayerSpellData(String s, List<String> strings) {
        this.playerClass = s;
        this.unlockedSpells = new HashSet<>(strings);
    }

    public String getPlayerClass() {
        return playerClass;
    }

    public void setPlayerClass(String newClass) {
        playerClass = newClass;
    }

    public void unlockSpell(String spellId) {
        unlockedSpells.add(spellId);
    }

    public boolean hasSpell(String spellId) {
        return unlockedSpells.contains(spellId);
    }

    public static final MapCodec<PlayerSpellData> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            Codec.STRING.fieldOf("player_class")
                                    .forGetter(PlayerSpellData::getPlayerClass),
                            Codec.STRING.listOf().fieldOf("spells")
                                    .forGetter(data -> List.copyOf(data.unlockedSpells))
                    ).apply(instance, PlayerSpellData::new)
            );

}
