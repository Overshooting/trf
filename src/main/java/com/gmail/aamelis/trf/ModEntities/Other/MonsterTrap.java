package com.gmail.aamelis.trf.ModEntities.Other;

import com.gmail.aamelis.trf.ModEntities.Projectiles.SpellProjectiles.PaintedPantheonProjectile;
import com.gmail.aamelis.trf.ModSpells.SpellDamageScaling;
import com.gmail.aamelis.trf.Registries.EntitiesInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class MonsterTrap extends Entity {

    private static final int LIFETIME = 500;
    private static final double RADIUS = 3.0;
    private static final int CHECK_INTERVAL = 5;

    private int perceptionStat;

    public MonsterTrap(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public MonsterTrap(Level level, int perceptionStat) {
        this(EntitiesInit.MONSTER_TRAP.get(), level);
        this.perceptionStat = perceptionStat;
    }

    @Override
    public void tick() {
        super.tick();

        if (!(level() instanceof ServerLevel serverLevel)) {
            return;
        }

        spawnParticles(serverLevel);

        if (tickCount % CHECK_INTERVAL == 0) {
            runDamageCheck(serverLevel);
        }

        if (tickCount > LIFETIME) {
            discard();
        }
    }

    private void spawnParticles(ServerLevel serverLevel) {
        for (int i = 0; i < 10; i++) {
            serverLevel.sendParticles(
                    ParticleTypes.ENCHANT,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    2,
                    random.nextGaussian() * 0.2, random.nextGaussian() * 0.2, random.nextGaussian() * 0.2,
                    0.1);
        }
    }

    private void runDamageCheck(ServerLevel serverLevel) {
        AABB area = new AABB(
                this.getX() - RADIUS,
                this.getY() - 0.5,
                this.getZ() - RADIUS,

                this.getX() + RADIUS,
                this.getY() + 2,
                this.getZ() + RADIUS
        );

        List<LivingEntity> availableEntities = serverLevel.getEntitiesOfClass(LivingEntity.class, area);

        boolean found = false;

        for (LivingEntity livingEntity : availableEntities) {
            if (livingEntity instanceof Player || livingEntity instanceof ServerPlayer) continue;

            double dx = livingEntity.getX() - getX();
            double dz = livingEntity.getZ() - getZ();

            if ((dx * dx  + dz * dz) > (RADIUS * RADIUS)) {
                continue;
            }

            DamageSource source = damageSources().magic();

            livingEntity.hurt(source, SpellDamageScaling.scaleDamage(0.5f, perceptionStat));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 100, 100));

            serverLevel.playSound(null, blockPosition(), SoundEvents.ANVIL_BREAK, SoundSource.NEUTRAL, 60.0f, 0.8f);

            found = true;
        }

        if (found) {
            discard();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        perceptionStat = valueInput.getIntOr("perception_stat", 0);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        valueOutput.putInt("perception_stat", perceptionStat);
    }
}
