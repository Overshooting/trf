package com.gmail.aamelis.trf.ModEntities.Projectiles.SpellProjectiles;

import com.gmail.aamelis.trf.ModPlayerData.ModStats.PlayerStatData;
import com.gmail.aamelis.trf.ModSpells.SpellDamageScaling;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class HyacinthBladeProjectile extends ThrowableProjectile {

    private float angleOffset;

    public HyacinthBladeProjectile(EntityType<? extends ThrowableProjectile> p_37466_, Level p_37467_) {
        super(p_37466_, p_37467_);

        angleOffset = 0;
    }

    public HyacinthBladeProjectile(Level level, LivingEntity shooter, float offset) {
        super(EntitiesInit.HYACINTH_BLADE_PROJECTILE.get(), level);

        setOwner(shooter);
        angleOffset = offset;
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) return;

        Entity owner = getOwner();

        if (owner == null || tickCount >= 300) {
            discard();
            return;
        }

        float orbitSpeed = 0.2f;
        float orbitRadius = 1.5f;

        double angle = tickCount * orbitSpeed + angleOffset;

        Vec3 basePos = owner.position().add(owner.getDeltaMovement());

        double x = basePos.x + Math.cos(angle) * orbitRadius;
        double z = basePos.z + Math.sin(angle) * orbitRadius;
        double y = basePos.y + owner.getBbHeight() * 0.75;

        Vec3 prevPos = position();
        setPos(x, y, z);

        AABB sweepBox = new AABB(prevPos, position()).inflate(0.5);

        for (LivingEntity target : level().getEntitiesOfClass(LivingEntity.class, sweepBox)) {
            if (target == owner || target instanceof Player) continue;

            onHitEntity(new EntityHitResult(target));
            break;
        }

        if (tickCount % 4 == 0) {
            spawnParticles();
        }
    }
    
    @Override
    public void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!(level() instanceof ServerLevel level)) return;

        Entity entity = result.getEntity();
        Entity owner = getOwner();

        if (!(owner instanceof ServerPlayer player)) return;

        if (entity instanceof LivingEntity target && !(entity instanceof Player)) {
            Vec3 pushDir = target.position().subtract(owner.position()).normalize();

            double strength = 1.2;

            target.setDeltaMovement(
                    pushDir.x * strength,
                    0.4,
                    pushDir.z * strength
            );

            PlayerStatData data = player.getData(AttachmentTypesInit.PLAYER_STATS);

            target.hurt(damageSources().indirectMagic(owner, this), SpellDamageScaling.scaleDamage(6.0f, data.getMagic()));

            burstParticles();
            level.playSound(null, blockPosition(),
                    SoundEvents.BEACON_DEACTIVATE,
                    SoundSource.PLAYERS,
                    60.0f, 0.8f);

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
                ParticleTypes.SMALL_FLAME,
                getX(),
                getY(),
                getZ(),
                4,
                random.nextGaussian() * 0.01, random.nextGaussian() * 0.01, random.nextGaussian() * 0.01,
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
