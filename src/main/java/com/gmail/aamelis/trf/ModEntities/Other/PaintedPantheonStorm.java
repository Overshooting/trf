package com.gmail.aamelis.trf.ModEntities.Other;

import com.gmail.aamelis.trf.ModEntities.Projectiles.PaintedPantheonProjectile;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class PaintedPantheonStorm extends Entity {

    private static final int LIFETIME = 100;
    private static final int DAMAGE_INTERVAL = 20;

    private static final double RADIUS = 5.0;
    private static final double HEIGHT = 7.0;

    public PaintedPantheonStorm(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();

        if (!(level() instanceof ServerLevel level)) return;

        spawnRainParticles(level);

        if (tickCount % DAMAGE_INTERVAL == 0) {
            pulseDamage(level);
            spawnDamageBurst(level);
        }

        if (tickCount >= LIFETIME) {
            discard();
        }
    }

    private void pulseDamage(ServerLevel level) {
        AABB area = new AABB(
                getX() - RADIUS,
                getY() - HEIGHT,
                getZ() - RADIUS,

                getX() + RADIUS,
                getY() + 1,
                getZ() + RADIUS
        );

        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);

        for (LivingEntity living : entities) {
            if (living instanceof Player || living instanceof ServerPlayer) continue;

            double dx = living.getX() - getX();
            double dz = living.getZ() - getZ();

            if ((dx * dx  + dz * dz) > (RADIUS * RADIUS)) {
                continue;
            }

            DamageSource source = damageSources().magic();

            living.hurt(source, 4.0f);
        }
    }

    private void spawnRainParticles(ServerLevel level) {
        for (int i = 0; i < 25; i++) {
            double x = getX() + random.nextDouble() * (RADIUS * 2) - RADIUS;
            double y = getY();
            double z = getZ() + random.nextDouble() * (RADIUS * 2) - RADIUS;

            SimpleParticleType particle = PaintedPantheonProjectile.TEXTURES[(random.nextInt(4))];

            level.sendParticles(
                    particle,
                    x,
                    y,
                    z,
                    0,
                    random.nextGaussian() * 0.2, -0.45, random.nextGaussian() * 0.02,
                    1.0
            );
        }
    }

    private void spawnDamageBurst(ServerLevel level) {
        level.sendParticles(
                PaintedPantheonProjectile.TEXTURES[(random.nextInt(4))],
                getX(),
                getY() + 1,
                getZ(),
                5,
                1, 0.2, 1,
                0.01
        );

        level.sendParticles(
                PaintedPantheonProjectile.TEXTURES[(random.nextInt(4))],
                getX(),
                getY() - 1,
                getZ(),
                3,
                1,
                0.2,
                1,
                0.01
        );

        level.sendParticles(
                PaintedPantheonProjectile.TEXTURES[(random.nextInt(4))],
                getX(),
                getY() - 1,
                getZ(),
                2,
                1,
                0.2,
                1,
                0.01
        );
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

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {

    }
}
