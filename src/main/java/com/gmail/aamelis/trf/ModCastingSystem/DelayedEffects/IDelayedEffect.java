package com.gmail.aamelis.trf.ModCastingSystem.DelayedEffects;

import net.minecraft.server.level.ServerLevel;

@FunctionalInterface
public interface IDelayedEffect {
    void apply(ServerLevel level);
}
