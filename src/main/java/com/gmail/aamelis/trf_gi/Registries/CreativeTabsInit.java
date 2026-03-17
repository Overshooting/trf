package com.gmail.aamelis.trf_gi.Registries;

import com.gmail.aamelis.trf_gi.TRFGearAndItemsFinalRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CreativeTabsInit {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TRFGearAndItemsFinalRegistry.MODID);

    public static final Supplier<CreativeModeTab> ALL_INGREDIENTS_TAB = CREATIVE_MODE_TABS.register("trf_gi_all_ingredients_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ItemsInit.BASIC_STAFF_ITEM.get()))
                    .title(Component.translatable("creativetab.trf_gi.all_ingredients"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ItemsInit.BASIC_STAFF_ITEM);

                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
