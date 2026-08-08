package com.gmail.aamelis.trf.ModEntities.Projectiles;

import com.gmail.aamelis.trf.ModPlayerData.ModStats.PlayerStatData;
import com.gmail.aamelis.trf.ModSpells.SpellDamageScaling;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockTypes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class ProjectileUtils {

    public static boolean validateBlock(Block block) {
        return block != Blocks.SHORT_GRASS &&
                block != Blocks.TALL_GRASS &&
                block != Blocks.TALL_DRY_GRASS &&
                block != Blocks.SHORT_DRY_GRASS &&
                block != Blocks.SNOW &&
                block != Blocks.SEAGRASS;
    }

    public static void applyExplosion(Projectile exploding, ServerLevel level, Vec3 location, @Nullable LivingEntity firstTarget, double radius) {
        AABB box = new AABB(location, location).inflate(radius);

        Entity owner = exploding.getOwner();

        if (!(owner instanceof ServerPlayer player)) return;

        PlayerStatData data = player.getData(AttachmentTypesInit.PLAYER_STATS);

        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                box,
                entity -> entity != owner && !(entity instanceof ServerPlayer) &&
                        (firstTarget == null || entity != firstTarget)
        );

        for (LivingEntity target : targets) {
            if (target.position().distanceToSqr(location) > radius * radius) continue;

            double distSqr = target.position().distanceToSqr(location);
            if (distSqr > radius * radius) return;

            Vec3 start = location;
            Vec3 end = target.getEyePosition();

            ClipContext context = new ClipContext(
                    start,
                    end,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    exploding
            );

            BlockHitResult result = level.clip(context);

            double blockHitMultiplier = 1.0;

            if (result.getType() == HitResult.Type.BLOCK) {
                Block block = level.getBlockState(result.getBlockPos()).getBlock();

                if (!ProjectileUtils.validateBlock(block)) break;

                double hitDist = result.getLocation().distanceToSqr(start);
                double targetDist = end.distanceToSqr(start);

                if (hitDist + 1e-4 < targetDist) blockHitMultiplier = 0.2;
            }

            double dist = Math.sqrt(target.position().distanceToSqr(location));

            float base = SpellDamageScaling.scaleDamage(6.0f, data.getMagic());

            float damage = Math.max(1.0f, (float)(base * (1.0 - dist / radius) * blockHitMultiplier));

            Vec3 knockback = target.position().subtract(location).normalize().scale(0.5);

            target.hurt(exploding.damageSources().indirectMagic(exploding, owner), damage);
            target.push(knockback.x, 0.3, knockback.z);
        }
    }


}
