package com.gmail.aamelis.trf.ModEntities.Projectiles.ArrowProjectiles;

import com.gmail.aamelis.trf.ModEffects.Imbuements.ImbuementEffect;
import com.gmail.aamelis.trf.ModPlayerData.ModStats.PlayerStatData;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

public class AbstractImbueableArrow extends AbstractArrow {

    private final float damage;

    public AbstractImbueableArrow(EntityType<? extends AbstractArrow> p_331098_, Level p_331626_) {
        super(p_331098_, p_331626_);

        damage = 0f;
    }

    public AbstractImbueableArrow(EntityType<? extends AbstractArrow> type, Level level, float damage) {
        super(type, level);

        this.damage = damage;
    }

    public AbstractImbueableArrow(EntityType<? extends AbstractArrow> type, LivingEntity shooter, Level level, float damage) {
        super(type, shooter, level, ItemStack.EMPTY, null);

        this.damage = damage;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        Entity owner = getOwner();
        Entity target = result.getEntity();
        if (!(owner instanceof ServerPlayer player) || !(target instanceof LivingEntity living)) return;

        Collection<MobEffectInstance> activeEffects = player.getActiveEffects();
        ArrayList<MobEffectInstance> effectsToRemove = new ArrayList<>();
        boolean applied = false;

        for (MobEffectInstance instance : activeEffects) {
            if (instance.getEffect().value() instanceof ImbuementEffect effect) {
                System.out.println("Imbued Effect found!");

                if (!applied) {
                    effect.applyEffect(living, player);
                    applied = true;
                }

                effectsToRemove.add(instance);
            } else {
                System.out.println(instance.getEffect().value() + " not instance of ImbuementEffect");
            }
        }

        for (MobEffectInstance instance : effectsToRemove) {
            player.removeEffect(instance.getEffect());
        }

        PlayerStatData data = player.getData(AttachmentTypesInit.PLAYER_STATS);

        double thisDamage = damage * getDeltaMovement().length() * (1 + data.getPerception() * 0.05);

        living.hurt(damageSources().arrow(this, player), (float) thisDamage);
    }



    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.ARROW);
    }
}
