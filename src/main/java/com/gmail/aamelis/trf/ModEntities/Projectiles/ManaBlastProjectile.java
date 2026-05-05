package com.gmail.aamelis.trf.ModEntities.Projectiles;

import com.gmail.aamelis.trf.Registries.EntitiesInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.*;
import org.checkerframework.checker.units.qual.C;

import javax.annotation.Nullable;
import java.util.List;

public class ManaBlastProjectile extends ThrowableProjectile {

    public ManaBlastProjectile(EntityType<? extends ThrowableProjectile> p_37466_, Level p_37467_) {
        super(p_37466_, p_37467_);
    }

    public ManaBlastProjectile(Level level, LivingEntity shooter) {
        super(EntitiesInit.MANA_BLAST_PROJECTILE.get(), level);

        setOwner(shooter);
        setPos(shooter.getX(), shooter.getEyeY() - 0.5, shooter.getZ());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

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
    public void tick() {
        super.tick();

        if (!level().isClientSide()) {
            spawnParticles();
        }

        if (tickCount > 15) {
            discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        Entity target = result.getEntity();
        Entity owner = getOwner();

        if (target == owner) {
            return;
        }

        if (target instanceof LivingEntity living && !(target instanceof ServerPlayer)) {
            living.hurt(damageSources().indirectMagic(this, owner), 8.0f);

            burstParticles();
            runHitResult(result.getLocation(), living);
        }

        discard();
    }

    private void runHitResult(Vec3 location, @Nullable LivingEntity firstTarget) {
        if (!(level() instanceof ServerLevel level)) return;

        double radius = 3.0;

        AABB box = new AABB(
                location.x() - radius, location.y() - radius, location.z() - radius,
                location.x() - radius, location.y() - radius, location.z() - radius
        );

        Entity owner = getOwner();

        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                box,
                entity -> entity != owner && !(entity instanceof ServerPlayer) &&
                        (firstTarget == null || entity != firstTarget)
        );

        for (LivingEntity target : targets) {
            if (target.position().distanceToSqr(location) > radius * radius) continue;

            Vec3 start = location;
            Vec3 end = target.getEyePosition();

            ClipContext context = new ClipContext(
                    start,
                    end,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    this
            );

            BlockHitResult result = level.clip(context);

            if (result.getType() != HitResult.Type.MISS) {
                double hitDist = result.getLocation().distanceToSqr(start);
                double targetDist = end.distanceToSqr(start);

                if (hitDist < targetDist) continue;
            }

            double dist = Math.sqrt(target.position().distanceToSqr(location));
            float damage = Math.max(1.0f, (float)(8.0 * (1.0 - dist / radius)));

            Vec3 knockback = target.position().subtract(location).normalize().scale(0.5);

            target.hurt(damageSources().indirectMagic(this, owner), damage);
            target.push(knockback.x, 0.3, knockback.z);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        Block block = level().getBlockState(result.getBlockPos()).getBlock();

        if (block != Blocks.SHORT_GRASS && block != Blocks.TALL_GRASS && block != Blocks.TALL_DRY_GRASS && block != Blocks.SHORT_DRY_GRASS &&
                block != Blocks.SNOW && block != Blocks.SEAGRASS) {
            runHitResult(result.getLocation(), null);
            burstParticles();
            discard();
        }
    }

    private void spawnParticles() {
        if (!(level() instanceof ServerLevel level)) return;

        level.sendParticles(
                ParticleTypes.FALLING_WATER,
                getX(),
                getY(),
                getZ(),
                6,
                random.nextGaussian() * 0.1, random.nextGaussian() * 0.1, random.nextGaussian() * 0.1,
                0.01
        );
    }

    private void burstParticles() {
        if (!(level() instanceof ServerLevel level)) return;

        level.sendParticles(
                ParticleTypes.RAIN,
                getX(),
                getY(),
                getZ(),
                40,
                0.6,0.6,0.6,
                0.3
        );
    }
}
