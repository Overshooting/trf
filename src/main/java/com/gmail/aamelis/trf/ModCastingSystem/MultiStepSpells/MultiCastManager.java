package com.gmail.aamelis.trf.ModCastingSystem.MultiStepSpells;

import com.gmail.aamelis.trf.ModSpells.ISpell;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultiCastManager {

    private static final List<SpellCastTask> TASKS = new ArrayList<>();

    public static void start(ServerPlayer player, ISpell spell) {
        TASKS.removeIf(t -> t.player == player && t.spell == spell);
        TASKS.add(new SpellCastTask(player, spell));
    }

    public static void tick() {
        Iterator<SpellCastTask> it = TASKS.iterator();

        while (it.hasNext()) {
            SpellCastTask task = it.next();

            if (task.player.isRemoved()) {
                it.remove();
                continue;
            }

            if (task.ticksRemaining-- <= 0) {
                task.spell.repeatedCast(task.player, task.iteration);

                task.iteration++;

                if (task.iteration >= task.spell.repetitions()) {
                    it.remove();
                    continue;
                }

                task.ticksRemaining = task.spell.multiCastTicks();
            }
        }
    }

}
