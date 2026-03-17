package com.gmail.aamelis.trf_gi.Registries;

import com.gmail.aamelis.trf_gi.ModItems.Weapons.AbstractStaffItem;
import com.gmail.aamelis.trf_gi.ModItems.Weapons.BasicStaffItem;
import com.gmail.aamelis.trf_gi.TRFGearAndItemsFinalRegistry;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemsInit {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TRFGearAndItemsFinalRegistry.MODID);

    public static final DeferredItem<Item> BASIC_STAFF_ITEM = ITEMS.registerItem("basic_staff", BasicStaffItem::new, AbstractStaffItem.STAFF_PROPERTIES);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
