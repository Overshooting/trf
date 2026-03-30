package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModScreens.GameMasterBlockMenu;
import net.minecraft.world.inventory.MenuType;

import javax.annotation.Nullable;

public class MenuTypesInit {
    public static final MenuType<?> GAME_MASTER_BLOCK_MENU = new standIn().get();
}

class standIn {
    @Nullable
    public MenuType<?> get() {
        return null;
    }
}
