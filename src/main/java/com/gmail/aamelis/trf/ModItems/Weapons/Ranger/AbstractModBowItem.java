package com.gmail.aamelis.trf.ModItems.Weapons.Ranger;

import com.gmail.aamelis.trf.ModEntities.Projectiles.ArrowProjectiles.AbstractImbueableArrow;
import com.gmail.aamelis.trf.ModItems.Weapons.Ranger.Arrows.AbstractModArrowItem;
import com.gmail.aamelis.trf.ModItems.Weapons.Ranger.Arrows.CopperArrowItem;
import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf.Registries.ItemsInit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class AbstractModBowItem extends BowItem {

    private float drawPower;
    private float power;
    private int drawTime;

    public static final Supplier<Properties> PROPERTIES = () ->
            new Item.Properties().stacksTo(1);

    public AbstractModBowItem(Properties p_40660_) {
        super(p_40660_);
    }

    public AbstractModBowItem(Properties properties, float drawPower, float power, int drawTime) {
        super(properties);
        this.drawPower = drawPower;
        this.power = power;
        this.drawTime = drawTime;
    }

    @Override
    public int getUseDuration(ItemStack p_40680_, LivingEntity p_345962_) {
        return drawTime;
    }

    public float getFastPower(int charge) {
        float f = (float) charge / drawPower;
        f = (f * f * f * 2.0f) / 3.0f;

        if (f > 1.0f) {
            f = 1.0f;
        }

        return f;
    }

    @Override
    public boolean releaseUsing(ItemStack p_40667_, Level p_40668_, LivingEntity p_40669_, int p_40670_) {
        if (!(p_40669_ instanceof ServerPlayer player)) return false;

        int charge = getUseDuration(p_40667_, player) - p_40670_;

        float power = getFastPower(charge);

        if (power < 0.1f) return false;

        ItemStack ammoStack = findAmmo(player);

        if (ammoStack.isEmpty()) return false;

        if (!(ammoStack.getItem() instanceof ArrowItem item)) return false;

        if (!player.level().isClientSide()) {
            AbstractArrow arrow = item.createArrow(p_40668_, ammoStack, player, p_40667_);

            arrow.shootFromRotation(
                    player,
                    player.getXRot(),
                    player.getYRot(),
                    0.0f,
                    power * this.power,
                    1.0f
            );

            arrow.setCritArrow(power >= 1.0f);

            if (player.getAbilities().instabuild) {
                arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            } else {
                arrow.pickup = AbstractArrow.Pickup.ALLOWED;
            }

            p_40668_.addFreshEntity(arrow);
        }

        p_40668_.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0f,
                1.0f / (p_40668_.getRandom().nextFloat() * 0.4f + 1.2f) + power * 0.5f);

        if (!player.getAbilities().instabuild) {
            ammoStack.shrink(1);
            if (ammoStack.isEmpty()) {
                player.getInventory().removeItem(ammoStack);
            }
        }

        return true;
    }

    private ItemStack findAmmo(Player player) {
        for (ItemStack stack : player.getInventory().getNonEquipmentItems()) {
            if (stack.getItem() instanceof AbstractModArrowItem) {
                return stack;
            }
        }

        if (player.getAbilities().instabuild) {
            return new ItemStack(ItemsInit.COPPER_ARROW_ITEM.get());
        }

        return ItemStack.EMPTY;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack p_40678_) {
        return ItemUseAnimation.BOW;
    }

    @Override
    public InteractionResult use(Level p_40672_, Player p_40673_, InteractionHand p_40674_) {
        PlayerSpellData data = p_40673_.getData(AttachmentTypesInit.PLAYER_SPELL_DATA);
        if (data.getPlayerClass() != PlayerSpellData.ARCHER) return InteractionResult.FAIL;

        return super.use(p_40672_, p_40673_, p_40674_);
    }
}
