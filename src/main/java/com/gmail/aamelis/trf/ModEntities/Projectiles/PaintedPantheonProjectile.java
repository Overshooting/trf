package com.gmail.aamelis.trf.ModEntities.Projectiles;

import com.gmail.aamelis.trf.ModEntities.Other.PaintedPantheonStorm;
import com.gmail.aamelis.trf.Registries.EntitiesInit;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.io.Serial;

public class PaintedPantheonProjectile extends ThrowableProjectile {

    public static SimpleParticleType[] TEXTURES = {
            ParticleTypes.GLOW,
            ParticleTypes.COPPER_FIRE_FLAME,
            ParticleTypes.SMOKE,
            ParticleTypes.WAX_OFF
    };

    public PaintedPantheonProjectile(EntityType<? extends ThrowableProjectile> p_37466_, Level p_37467_) {
        super(p_37466_, p_37467_);
    }

    public PaintedPantheonProjectile(Level level, LivingEntity shooter) {
        super(EntitiesInit.PAINTED_PANTHEON_PROJECTILE.get(), level);

        setOwner(shooter);
        setPos(shooter.getX(), shooter.getEyeY() - 0.5, shooter.getZ());
    }

    @Override
    public void tick() {
        super.tick();

        spawnParticles();

        if (tickCount > 40) {
            explode();
        }
    }

    private void explode() {
        if (!(level() instanceof ServerLevel level)) {
            discard();
            return;
        }

        PaintedPantheonStorm storm = new PaintedPantheonStorm(
                EntitiesInit.PAINTED_PANTHEON_STORM.get(),
                level
        );

        storm.setPos(getX(), getY() + 7, getZ());

        level.addFreshEntity(storm);

        level.sendParticles(
                TEXTURES[(random.nextInt(4))],
                getX(),
                getY(),
                getZ(),
                20,
                1,1,1,
                0.2
        );

        level.sendParticles(
                TEXTURES[(random.nextInt(4))],
                getX(),
                getY(),
                getZ(),
                20,
                1,1,1,
                0.2
        );

        level.sendParticles(
                TEXTURES[(random.nextInt(4))],
                getX(),
                getY(),
                getZ(),
                20,
                1,1,1,
                0.2
        );

        discard();
    }

    private void spawnParticles() {
        if (!(level() instanceof ServerLevel level)) return;

        level.sendParticles(
                TEXTURES[(random.nextInt(4))],
                getX(),
                getY(),
                getZ(),
                4,
                0.05, 0.05, 0.05,
                0.01
        );

        level.sendParticles(
                TEXTURES[(random.nextInt(4))],
                getX(),
                getY(),
                getZ(),
                2,
                0.05, 0.05, 0.05,
                0.01
        );

        level.sendParticles(
                TEXTURES[(random.nextInt(4))],
                getX(),
                getY(),
                getZ(),
                2,
                0.05, 0.05, 0.05,
                0.01
        );
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        explode();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        if (ProjectileUtils.validateBlock(level().getBlockState(result.getBlockPos()).getBlock())) {
            explode();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }
}
