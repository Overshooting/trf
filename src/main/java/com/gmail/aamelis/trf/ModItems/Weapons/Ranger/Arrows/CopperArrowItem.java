package com.gmail.aamelis.trf.ModItems.Weapons.Ranger.Arrows;

import com.gmail.aamelis.trf.ModEntities.Projectiles.ArrowProjectiles.AbstractImbueableArrow;
import com.gmail.aamelis.trf.ModEntities.Projectiles.ArrowProjectiles.CopperArrow;
import com.gmail.aamelis.trf.Registries.EntitiesInit;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;
import java.util.function.Supplier;

public class CopperArrowItem extends AbstractModArrowItem {

    public static final Supplier<Properties> PROPERTIES = () ->
            new Item.Properties().stacksTo(64);

    public CopperArrowItem(Properties p_40512_) {
        super(p_40512_);
    }

    @Override
    public AbstractImbueableArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        if (!(shooter instanceof ServerPlayer sp)) return new CopperArrow(EntitiesInit.COPPER_ARROW.get(), level);

        return new CopperArrow(level, sp);
    }

    @Override
    public Projectile asProjectile(Level p_338330_, Position p_338329_, ItemStack p_338197_, Direction p_338469_) {
        CopperArrow arrow = new CopperArrow(p_338330_, null);

        arrow.setPos(p_338329_.x(), p_338329_.y(), p_338329_.z());

        arrow.shoot(
                p_338469_.getStepX(),
                p_338469_.getStepY(),
                p_338469_.getStepZ(),
                1.5f,
                1.0f
        );

        return arrow;
    }

    @Override
    public boolean isInfinite(ItemStack ammo, ItemStack bow, LivingEntity livingEntity) {
        return false;
    }


}
