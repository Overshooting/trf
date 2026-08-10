package com.gmail.aamelis.trf.ModEntities.Projectiles.SpellProjectiles;

import com.gmail.aamelis.trf.ModEntities.Projectiles.DamageUtils;
import com.gmail.aamelis.trf.ModPlayerData.ModStats.PlayerStatData;
import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
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
import net.minecraft.world.phys.*;

import javax.annotation.Nullable;

public class ManaBlastProjectile extends ThrowableProjectile {

    private static final float DAMAGE = 5.0f;

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

        if (!(owner instanceof ServerPlayer serverPlayer)) return;

        if (target == owner) return;

        if (target instanceof LivingEntity living && !(target instanceof ServerPlayer)) {
            PlayerStatData data = serverPlayer.getData(AttachmentTypesInit.PLAYER_STATS);

            living.hurt(damageSources().indirectMagic(this, owner), SpellDamageScaling.scaleDamage(7.0f, data.getMagic()));

            runHitResult(result.getLocation(), living, DAMAGE);
        }

        discard();
    }

    private void runHitResult(Vec3 location, @Nullable LivingEntity firstTarget, float damage) {
        if (!(level() instanceof ServerLevel level)) return;

        DamageUtils.applyExplosion(this, level, location, firstTarget, 4.0, damage, PlayerSpellData.MAGE);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        Block block = level().getBlockState(result.getBlockPos()).getBlock();

        if (DamageUtils.validateBlock(block)) {
            runHitResult(result.getLocation(), null, DAMAGE);
            discard();
        }
    }

    private void spawnParticles() {
        if (!(level() instanceof ServerLevel level)) return;

        level.sendParticles(
                ParticleTypes.SONIC_BOOM,
                getX(),
                getY(),
                getZ(),
                6,
                random.nextGaussian() * 0.1, random.nextGaussian() * 0.1, random.nextGaussian() * 0.1,
                0.01
        );
    }
}
