package com.gmail.aamelis.trf_gi.ModSpells;

import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public interface ISpell {

    String getId();

    String getRequiredClass();

    int getRequiredMana();

    void cast(ServerPlayer player);

    List<SpellInput> getCombo();
}
