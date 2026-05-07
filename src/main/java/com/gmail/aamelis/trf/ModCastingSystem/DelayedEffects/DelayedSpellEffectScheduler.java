package com.gmail.aamelis.trf.ModCastingSystem.DelayedEffects;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.*;

public class DelayedSpellEffectScheduler {

    private static final Map<ResourceKey<Level>, List<DelayedSpellEffect>> EFFECTS = new HashMap<>();

    public static void schedule(ServerLevel level, DelayedSpellEffect effect) {
        ResourceKey<Level> key = level.dimension();
        EFFECTS.computeIfAbsent(key, l -> new LinkedList<>()).add(effect);
    }

    public static void tick(ServerLevel level) {
        List<DelayedSpellEffect> list = EFFECTS.get(level.dimension());
        if (list == null) return;

        list.removeIf(effect -> effect.tick(level));
    }

}
