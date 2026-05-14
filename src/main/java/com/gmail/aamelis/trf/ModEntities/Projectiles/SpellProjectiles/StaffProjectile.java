package com.gmail.aamelis.trf.ModEntities.Projectiles.SpellProjectiles;

import com.gmail.aamelis.trf.ModEntities.Projectiles.ProjectileUtils;
import com.gmail.aamelis.trf.ModPlayerData.ModStats.PlayerStatData;
import com.gmail.aamelis.trf.ModSpells.SpellDamageScaling;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf.Registries.EntitiesInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class StaffProjectile extends ThrowableProjectile {

    private float damage;

    public StaffProjectile(EntityType<? extends StaffProjectile> p_37466_, Level p_37467_) {
        super(p_37466_, p_37467_);

        damage = 0.0f;
    }

    public StaffProjectile(Level level, LivingEntity shooter) {
        super(EntitiesInit.STAFF_PROJECTILE.get(), level);

        setOwner(shooter);
        setPos(shooter.getX(), shooter.getEyeY() - 0.5, shooter.getZ());
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
                ParticleTypes.SOUL,
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
                ParticleTypes.SOUL_FIRE_FLAME,
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
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        Entity target = result.getEntity();
        Entity owner = getOwner();

        if (!(owner instanceof ServerPlayer player)) return;

        if (target == owner) {
            return;
        }

        if (target instanceof LivingEntity living) {
            burstParticles();

            PlayerStatData data = player.getData(AttachmentTypesInit.PLAYER_STATS);

            living.hurt(damageSources().indirectMagic(this, owner), SpellDamageScaling.scaleDamage(damage, data.getMagic()));
        }

        discard();
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }
}
