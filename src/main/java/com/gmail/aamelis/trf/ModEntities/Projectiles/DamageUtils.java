package com.gmail.aamelis.trf.ModEntities.Projectiles;

import com.gmail.aamelis.trf.ModPlayerData.ModStats.PlayerStatData;
import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
import com.gmail.aamelis.trf.ModSpells.SpellDamageScaling;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class DamageUtils {

    public static boolean validateBlock(Block block) {
        return block != Blocks.SHORT_GRASS &&
                block != Blocks.TALL_GRASS &&
                block != Blocks.TALL_DRY_GRASS &&
                block != Blocks.SHORT_DRY_GRASS &&
                block != Blocks.SNOW &&
                block != Blocks.SEAGRASS;
    }

    public static void applyExplosion(Projectile exploding, ServerLevel level, Vec3 location, @Nullable LivingEntity firstTarget, double radius, float baseDamage, short classType) {
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

                if (!DamageUtils.validateBlock(block)) break;

                double hitDist = result.getLocation().distanceToSqr(start);
                double targetDist = end.distanceToSqr(start);

                if (hitDist + 1e-4 < targetDist) blockHitMultiplier = 0.2;
            }

            double dist = Math.sqrt(target.position().distanceToSqr(location));

            int scaleStat = getScaleStat(classType, data);

            float base = SpellDamageScaling.scaleDamage(baseDamage, scaleStat);

            float damage = Math.max(1.0f, (float)(base * (1.0 - dist / radius) * blockHitMultiplier));

            Vec3 knockback = target.position().subtract(location).normalize().scale(0.5);

            target.hurt(exploding.damageSources().indirectMagic(exploding, owner), damage);
            target.push(knockback.x, 0.3, knockback.z);
        }
    }

    private static int getScaleStat(short classType, PlayerStatData data) {
        return switch (classType) {
            case PlayerSpellData.MAGE -> data.getMagic();

            case PlayerSpellData.ARCHER -> data.getPerception();

            case PlayerSpellData.CLERIC -> data.getPiety();

            case PlayerSpellData.WARRIOR -> data.getStrength();

            default -> 0;
        };
    }

    public static AABB createSlashBox(ServerPlayer player, double range, double width, double height) {
        Vec3 origin = player.getEyePosition();
        Vec3 look = player.getLookAngle();

        Vec3 end = origin.add(look.scale(range));

        double minX = Math.min(origin.x, end.x) - width;
        double maxX = Math.max(origin.x, end.x) + width;

        double minY = player.getY();
        double maxY = player.getY() + height;

        double minZ = Math.min(origin.z, end.z) - width;
        double maxZ = Math.max(origin.z, end.z) + width;

        return new AABB(
                minX, minY, minZ,
                maxX, maxY, maxZ
        );
    }

    public static void applySlashHurtBox(AABB boundingBox, ServerPlayer owner, ServerLevel level, float baseDamage, short classType, double range, double maxAngleDegrees) {
        Vec3 look = owner.getLookAngle().normalize();
        Vec3 origin = owner.getEyePosition();
        double angleThreshold = Math.cos(Math.toRadians(maxAngleDegrees));

        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                boundingBox,
                entity ->
                        entity != owner
                                && !(entity instanceof ServerPlayer)
        );

        PlayerStatData data = owner.getData(AttachmentTypesInit.PLAYER_STATS);

        for (LivingEntity target : targets) {
            AABB targetBox = target.getBoundingBox();

            double closestX = Mth.clamp(origin.x, targetBox.minX, targetBox.maxX);
            double closestY = Mth.clamp(origin.y, targetBox.minY, targetBox.maxY);
            double closestZ = Mth.clamp(origin.z, targetBox.minZ, targetBox.maxZ);

            Vec3 closestPoint = new Vec3(
                    closestX,
                    closestY,
                    closestZ
            );

            Vec3 toTarget = closestPoint.subtract(origin);

            double distance = toTarget.length();

            if (distance > range) {
                continue;
            }

            if (distance <= 0.0001) {
                continue;
            }

            toTarget = toTarget.normalize();

            double dot = look.dot(toTarget);

            if (dot < angleThreshold) {
                continue;
            }

            float damage = SpellDamageScaling.scaleDamage(
                    baseDamage,
                    getScaleStat(classType, data)
            );

            target.hurt(
                    target.damageSources().playerAttack(owner),
                    damage
            );
        }
    }


}
