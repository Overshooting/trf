package com.gmail.aamelis.trf.ModEntities.Projectiles;

import com.gmail.aamelis.trf.Registries.EntitiesInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class HyacinthBladeProjectile extends ThrowableProjectile {


    public HyacinthBladeProjectile(EntityType<? extends ThrowableProjectile> p_37466_, Level p_37467_) {
        super(p_37466_, p_37467_);
    }

    public HyacinthBladeProjectile(Level level, LivingEntity shooter) {
        super(EntitiesInit.HYACINTH_BLADE_PROJECTILE.get(), level);

        setOwner(shooter);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) return;

        this.tickCount++;

        float orbitSpeed = 0.5f;
        float orbitRadius = 1.0f;

        if (getOwner() != null) {
            double angle = tickCount + orbitSpeed;
            double x = getOwner().getX() + Math.cos(angle) + orbitRadius;
            double z = getOwner().getZ() + Math.sin(angle) + orbitRadius;
            double y = getOwner().getEyeY() - 0.5;

            this.setPos(x, y, z);
        } else if (tickCount >= 200) {
            discard();
        }

        spawnParticles();
    }
    
    @Override
    public void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!(level() instanceof ServerLevel level)) return;

        Entity entity = result.getEntity();

        if (entity instanceof LivingEntity && !(entity instanceof Player)) {
            burstParticles();

            level.playSound(null, blockPosition(), SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 60.0f, 0.8f);

            entity.hurt(damageSources().indirectMagic(getOwner(), this), 4.0f);
            discard();
        }
    }

    @Override
    public void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
    }

    private void spawnParticles() {
        if (!(level() instanceof ServerLevel level)) return;

        level.sendParticles(
                ParticleTypes.CHERRY_LEAVES,
                getX(),
                getY(),
                getZ(),
                4,
                random.nextGaussian() * 0.05, random.nextGaussian() * 0.05, random.nextGaussian() * 0.05,
                0.01
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
