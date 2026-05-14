package com.gmail.aamelis.trf.ModEffects.Imbuements;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

public interface ImbuementEffect {

    void applyEffect(LivingEntity entity, ServerPlayer source);

}
