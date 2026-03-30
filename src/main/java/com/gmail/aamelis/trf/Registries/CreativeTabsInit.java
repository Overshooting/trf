package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CreativeTabsInit {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TRFFinalRegistry.MODID);

    public static final Supplier<CreativeModeTab> ALL_WEAPONS_TAB = CREATIVE_MODE_TABS.register("trf_all_weapons_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ItemsInit.BASIC_STAFF_ITEM.get()))
                    .title(Component.translatable("creativetab.trf.all_weapons"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ItemsInit.BASIC_STAFF_ITEM);

                    }).build());

    public static final Supplier<CreativeModeTab> ALL_BLOCKS_TAB = CREATIVE_MODE_TABS.register("trf_all_blocks_tab",
            () -> CreativeModeTab.builder()
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "trf_all_weapons_tab"))
                    .icon(() -> new ItemStack(ItemsInit.LIGHTS_OUT_BLOCK_ITEM.get()))
                    .title(Component.translatable("creativetab.trf.all_blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ItemsInit.GAME_MASTER_BLOCK_ITEM);
                        output.accept(ItemsInit.LIGHTS_OUT_BLOCK_ITEM);

                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
