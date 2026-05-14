package com.gmail.aamelis.trf.ModEntities.Projectiles.ArrowProjectiles;

import com.gmail.aamelis.trf.Registries.EntitiesInit;
import com.gmail.aamelis.trf.Registries.ItemsInit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class CopperArrow extends AbstractImbueableArrow {

    public CopperArrow(EntityType<? extends AbstractArrow> p_331098_, Level p_331626_) {
        super(p_331098_, p_331626_, 2.0f);
    }

    public CopperArrow(Level level, ServerPlayer shooter) {
        super(EntitiesInit.COPPER_ARROW.get(), shooter, level, 2.0f);

        if (shooter != null) {
            setOwner(shooter);
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ItemsInit.COPPER_ARROW_ITEM.get(), 1);
    }
}
