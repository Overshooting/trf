package com.gmail.aamelis.trf.Registries;


import com.gmail.aamelis.trf.ModScreens.GameMasterBlockMenu;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MenuTypesInit {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, TRFFinalRegistry.MODID);

    public static final Supplier<MenuType<GameMasterBlockMenu>> GAME_MASTER_BLOCK_MENU =
            MENU_TYPES.register("game_master_block_menu", () -> IMenuTypeExtension.create(GameMasterBlockMenu::new));

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }

}
