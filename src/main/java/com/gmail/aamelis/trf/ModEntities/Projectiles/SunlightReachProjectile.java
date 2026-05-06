package com.gmail.aamelis.trf.ModEntities.Projectiles;

import com.gmail.aamelis.trf.Registries.EntitiesInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;

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
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        Block block = level().getBlockState(result.getBlockPos()).getBlock();

        if (ProjectileUtils.validateBlock(block)) {
            burstParticles();
            discard();
        }
    }

    private void spawnParticles() {
        if (!(level() instanceof ServerLevel level)) return;

        level.sendParticles(
                ParticleTypes.ASH,
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
                ParticleTypes.COPPER_FIRE_FLAME,
                getX(),
                getY(),
                getZ(),
                40,
                0.5,0.5,0.5,
                0.3
        );
    }

    @Override
    public void defineSynchedData(SynchedEntityData.Builder builder) {

    }
}
