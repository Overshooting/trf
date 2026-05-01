package com.gmail.aamelis.trf.ModCastingSystem.MultiStepSpells;

import com.gmail.aamelis.trf.ModSpells.ISpell;
import net.minecraft.server.level.ServerPlayer;

public class SpellCastTask {

    public final ServerPlayer player;
    public final ISpell spell;

    public int ticksRemaining;
    public int iteration = 0;

    public SpellCastTask(ServerPlayer player, ISpell spell) {
        this.player = player;
        this.spell = spell;
        this.ticksRemaining = 0;
    }

}
