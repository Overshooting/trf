package com.gmail.aamelis.trf.ModCastingSystem.DelayedEffects;

import net.minecraft.server.level.ServerLevel;

public class DelayedSpellEffect {

    private int ticksRemaining;
    private final IDelayedEffect effect;

    public DelayedSpellEffect(int delayTicks, IDelayedEffect effect) {
        this.ticksRemaining = delayTicks;
        this.effect = effect;
    }

    public boolean tick(ServerLevel level) {
        ticksRemaining--;

        if (ticksRemaining <= 0) {
           effect.apply(level);
           return true;
        }

        return false;
    }

}
