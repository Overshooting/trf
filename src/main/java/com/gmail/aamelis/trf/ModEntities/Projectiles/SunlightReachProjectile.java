package com.gmail.aamelis.trf.ModEntities.Projectiles;

import com.gmail.aamelis.trf.ModCastingSystem.DelayedEffects.DelayedSpellEffect;
import com.gmail.aamelis.trf.ModCastingSystem.DelayedEffects.DelayedSpellEffectScheduler;
import com.gmail.aamelis.trf.Registries.EntitiesInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class SunlightReachProjectile extends ThrowableProjectile {

    public SunlightReachProjectile(EntityType<? extends ThrowableProjectile> p_37466_, Level p_37467_) {
        super(p_37466_, p_37467_);
    }

    public SunlightReachProjectile(Level level, LivingEntity shooter) {
        super(EntitiesInit.SUNLIGHT_REACH_PROJECTILE.get(), level);

        setOwner(shooter);
        setPos(shooter.getX(), shooter.getEyeY() - 0.5, shooter.getZ());
    }

    @Override
    public void tick() {
        super.tick();

        spawnParticles();

        if (tickCount > 15) {
            discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!(level() instanceof ServerLevel serverLevel)) return;

        Entity target = result.getEntity();

        if (target instanceof LivingEntity living) {
            living.hurt(damageSources().indirectMagic(getOwner(), target), 3.0f);

            DelayedSpellEffectScheduler.schedule(serverLevel,
                    new DelayedSpellEffect(40, (lvl) -> {
                        Entity e = lvl.getEntity(living.getUUID());
                        if (e instanceof LivingEntity l && l.isAlive()) {
                            l.hurt(l.damageSources().magic(), 5.0f);

                            burstParticles(l.position());
                        }
                    })
            );

            discard();
        }

    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        Block block = level().getBlockState(result.getBlockPos()).getBlock();

        if (ProjectileUtils.validateBlock(block)) {
            burstParticles(position());
            discard();
        }
    }

    private void spawnParticles() {
        if (!(level() instanceof ServerLevel level)) return;

        level.sendParticles(
                ParticleTypes.WAX_ON,
                getX(),
                getY(),
                getZ(),
                8,
                random.nextGaussian() * 0.1, random.nextGaussian() * 0.1, random.nextGaussian() * 0.1,
                0.05
        );
    }

    private void burstParticles(Vec3 pos) {
        if (!(level() instanceof ServerLevel level)) return;

        level.sendParticles(
                ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER,
                pos.x(),
                pos.y(),
                pos.z(),
                40,
                0.4,0.4,0.4,
                0.3
        );
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean canUsePortal(boolean p_352918_) {
        return false;
    }

    @Override
    public void defineSynchedData(SynchedEntityData.Builder builder) {

    }
}
