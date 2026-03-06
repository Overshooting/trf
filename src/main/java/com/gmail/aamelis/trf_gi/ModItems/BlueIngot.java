package com.gmail.aamelis.trf_gi.ModItems;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class BlueIngot extends Item {

    public static final Supplier<Properties> BLUE_INGOT_PROPERTIES =
            () -> new Item.Properties().stacksTo(8);

    public BlueIngot(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        return super.getName(stack).copy().withStyle(ChatFormatting.AQUA);
    }
}
