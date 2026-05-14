package com.gmail.aamelis.trf.ModItems.Weapons.Ranger.Arrows;

import com.gmail.aamelis.trf.ModEntities.Projectiles.ArrowProjectiles.AbstractImbueableArrow;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractModArrowItem extends ArrowItem {
    public AbstractModArrowItem(Properties p_40512_) {
        super(p_40512_);
    }

    @Override
    public abstract AbstractImbueableArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon);
}
