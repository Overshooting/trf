package com.gmail.aamelis.trf.ModEntities.Projectiles;

import com.gmail.aamelis.trf.Registries.EffectsInit;
import com.gmail.aamelis.trf.Registries.EntitiesInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LightningBeamProjectile extends ThrowableProjectile {

    private final Set<Entity> hitEntities = new HashSet<>();

    public LightningBeamProjectile(EntityType<? extends ThrowableProjectile> p_37466_, Level p_37467_) {
        super(p_37466_, p_37467_);
    }

    public LightningBeamProjectile(Level level, LivingEntity shooter) {
        super(EntitiesInit.LIGHTNING_BEAM_PROJECTILE.get(), level);

        setOwner(shooter);
        setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
    }

    @Override
    public void tick() {
        super.tick();

        Vec3 start = position();
        Vec3 motion = getDeltaMovement();
        Vec3 end = start.add(motion);

        HitResult blockHit = level().clip(new ClipContext(
                start,
                end,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                this
        ));

        if (blockHit.getType() != HitResult.Type.MISS) {
            end = blockHit.getLocation();
            impactParticles(end);
            discard();
        }

        AABB box = new AABB(start, end).inflate(0.75);

        List<LivingEntity> targets = level().getEntitiesOfClass(
                LivingEntity.class,
                box,
                e -> !(e instanceof Player)  && e.isAlive() && e != getOwner()
        );

        for (LivingEntity entity : targets) {

            if (hitEntities.contains(entity)) continue;

            if (entity.getBoundingBox().clip(start, end).isPresent()) {

                DamageSource source = damageSources().indirectMagic(this, getOwner());

                float damage = 6.0f;
                entity.hurt(source, damage);

                entity.addEffect(new MobEffectInstance(
                        EffectsInit.SHOCKED_EFFECT,
                        100,
                        0
                ));

                hitEntities.add(entity);
            }
        }

        setPos(end.x, end.y, end.z);

        if (level().isClientSide()) {
            spawnTrailParticles();
        }

        if (tickCount > 5) {
            discard();
        }
    }

    private void spawnTrailParticles() {
        for (int i = 0; i < 4; i++) {
            level().addParticle(
                    ParticleTypes.ELECTRIC_SPARK,
                    getX(),
                    getY(),
                    getZ(),
                    random.nextGaussian() * 0.05,
                    random.nextGaussian() * 0.05,
                    random.nextGaussian() * 0.05
            );
        }
    }

    private void impactParticles(Vec3 pos) {
        if (!(level() instanceof ServerLevel server)) return;

        server.sendParticles(
                ParticleTypes.ELECTRIC_SPARK,
                pos.x, pos.y, pos.z,
                20,
                0.3, 0.3, 0.3,
                0.1
        );
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }
}
