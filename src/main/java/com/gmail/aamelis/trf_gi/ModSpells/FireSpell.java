package com.gmail.aamelis.trf_gi.ModSpells;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class FireSpell implements ISpell {

    @Override
    public String getId() {
        return "fire";
    }

    @Override
    public String getRequiredClass() {
        return "default";
    }


    @Override
    public void cast(ServerPlayer player) {
        player.sendSystemMessage(Component.literal("Fire Spell Cast!"));
    }

    @Override
    public List<SpellInput> getCombo() {
        return List.of(
                SpellInput.C,
                SpellInput.C,
                SpellInput.C
        );
    }


}
