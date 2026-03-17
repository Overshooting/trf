package com.gmail.aamelis.trf_gi.ModEntities;

import com.gmail.aamelis.trf_gi.Registries.EntitiesInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.fml.startup.Server;

public class StaffProjectile extends ThrowableProjectile {

    private float damage = 0;

    public StaffProjectile(EntityType<? extends StaffProjectile> p_37466_, Level p_37467_) {
        super(p_37466_, p_37467_);
    }

    public StaffProjectile(Level level, LivingEntity shooter) {
        super(EntitiesInit.STAFF_PROJECTILE.get(), level);

        setOwner(shooter);
        setPos(shooter.getX(), shooter.getEyeY() - 0.5, shooter.getZ());
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide()) {
            spawnParticles();
        }

        if (tickCount > 15) {
            discard();
        }
    }

    private void spawnParticles() {
        for (int i = 0; i < 2; i++) {
            level().addParticle(
                    ParticleTypes.SOUL,
                    getX(),
                    getY(),
                    getZ(),
                    random.nextGaussian() * 0.1,
                    random.nextGaussian() * 0.1,
                    random.nextGaussian() * 0.1
            );
        }
    }

    private void burstParticles() {
        if (!(level() instanceof ServerLevel)) return;

        for (int i = 0; i < 20; i++) {
            ((ServerLevel) level()).sendParticles(
                    ParticleTypes.SOUL_FIRE_FLAME,
                    getX(),
                    getY(),
                    getZ(),
                    20,
                    0.2,0.2,0.2,
                    0.05
            );
        }
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
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        Entity target = result.getEntity();
        Entity owner = getOwner();

        if (target == owner) {
            return;
        }

        if (target instanceof LivingEntity living) {

            DamageSource source = damageSources().indirectMagic(this, owner);

            burstParticles();

            living.hurt(source, damage);
        }

        discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        Block block = level().getBlockState(result.getBlockPos()).getBlock();

        if (block != Blocks.SHORT_GRASS && block != Blocks.TALL_GRASS && block != Blocks.TALL_DRY_GRASS && block != Blocks.SHORT_DRY_GRASS &&
                block != Blocks.SNOW && block != Blocks.SEAGRASS) {
            burstParticles();
            discard();
        }
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }
}
