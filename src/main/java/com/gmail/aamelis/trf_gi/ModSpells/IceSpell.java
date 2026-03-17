package com.gmail.aamelis.trf_gi.ModSpells;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class IceSpell implements ISpell {

    @Override
    public String getId() {
        return "ice";
    }

    @Override
    public String getRequiredClass() {
        return "default";
    }

    @Override
    public List<SpellInput> getCombo() {
        return List.of(
                SpellInput.C,
                SpellInput.C,
                SpellInput.V
        );
    }

    @Override
    public void cast(ServerPlayer player) {
        player.sendSystemMessage(Component.literal("Ice Spell Cast!"));
    }


}
