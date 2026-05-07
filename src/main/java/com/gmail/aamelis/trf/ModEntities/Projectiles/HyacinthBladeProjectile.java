package com.gmail.aamelis.trf.ModEntities.Projectiles;

import com.gmail.aamelis.trf.Registries.EntitiesInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class HyacinthBladeProjectile extends ThrowableProjectile {


    public HyacinthBladeProjectile(EntityType<? extends ThrowableProjectile> p_37466_, Level p_37467_) {
        super(p_37466_, p_37467_);
    }

    public HyacinthBladeProjectile(Level level, LivingEntity shooter) {
        super(EntitiesInit.HYACINTH_BLADE_PROJECTILE.get(), level);

        setOwner(shooter);
        setPos(shooter.getX(), shooter.getEyeY() - 0.5, shooter.getZ());
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) return;

        this.tickCount++;

        float orbitSpeed = 0.5f;
        float orbitRadius = 0.5f;

        if (getOwner() != null) {
            double angle = tickCount + orbitSpeed;
            double x = getOwner().getX() + Math.cos(angle) + orbitRadius;
            double z = getOwner().getZ() + Math.sin(angle) + orbitRadius;
            double y = getOwner().getEyeY() - 0.5;

            this.setPos(x, y, z);
        } else {
            discard();
        }

        spawnParticles();
    }
    
    @Override
    public void onHitEntity(EntityHitResult result) {

    }

    @Override

    private void spawnParticles() {
        if (!(level() instanceof ServerLevel level)) return;

        level.sendParticles(
                ParticleTypes.CHERRY_LEAVES,
                getX(),
                getY(),
                getZ(),
                6,
                random.nextGaussian() * 0.1, random.nextGaussian() * 0.1, random.nextGaussian() * 0.1,
                0.05
        );
    }

    private void burstParticles() {
        if (!(level() instanceof ServerLevel level)) return;

        level.sendParticles(
                ParticleTypes.CHERRY_LEAVES,
                getX(),
                getY(),
                getZ(),
                40,
                0.5,0.5,0.5,
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
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }
}
