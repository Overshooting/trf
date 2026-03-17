package com.gmail.aamelis.trf_gi.ModItems.Weapons;

import com.gmail.aamelis.trf_gi.ModEntities.StaffProjectile;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class AbstractStaffItem extends Item {

    private static final int COOLDOWN_TICKS = 10;
    private float staffDamage;

    public static final Supplier<Properties> STAFF_PROPERTIES = () ->
            new Item.Properties().stacksTo(1);

    public AbstractStaffItem(Properties properties, float staffDamage) {
        super(properties);

        this.staffDamage = staffDamage;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(stack) || !canCast(player, stack)) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide()) {
            castSpell(level, player);
        }

        player.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 1.0f, 1.0f);

        return InteractionResult.SUCCESS;
    }

    private boolean canCast(Player player, ItemStack stack) {
        return true;
    }

    private void castSpell(Level level, Player player) {
        StaffProjectile projectile = new StaffProjectile(level, player);

        projectile.setDamage(staffDamage);

        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 2.0f, 0.1f);

        level.addFreshEntity(projectile);
    }
}
