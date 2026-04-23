package com.gmail.aamelis.trf.ModSpells;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public interface ISpell {

    String getId();

    String getDisplayName();

    short getRequiredClass();

    int getRequiredMana();

    long getCooldown();

    void cast(ServerPlayer player);

    List<SpellInput> getCombo();

    ResourceLocation getFullPath();
    ResourceLocation getEmptyPath();
    ResourceLocation animationId();
}