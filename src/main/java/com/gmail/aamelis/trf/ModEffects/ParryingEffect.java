package com.gmail.aamelis.trf.ModEffects;

import com.gmail.aamelis.trf.Registries.EffectsInit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class ParryingEffect extends MobEffect {

    public ParryingEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onMobHurt(ServerLevel level, LivingEntity entity, int amplifier, DamageSource damageSource, float amount) {
        if (entity instanceof ServerPlayer player) {
            if (player.hasEffect(EffectsInit.PARRYING_EFFECT)) {
                Entity damaging = damageSource.getEntity();

                if (damaging instanceof LivingEntity livingEntity) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 20, 255));
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 255));

                    player.setKnownMovement(new Vec3(0, 0, 0));

                    player.level().playSound(null, player.blockPosition(), SoundEvents.ANVIL_HIT, SoundSource.PLAYERS, 1.0f, 1.0f);
                }
            }
        }
    }
}
