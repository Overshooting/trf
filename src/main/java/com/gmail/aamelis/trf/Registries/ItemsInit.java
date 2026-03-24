package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModItems.Weapons.AbstractStaffItem;
import com.gmail.aamelis.trf.ModItems.Weapons.BasicStaffItem;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemsInit {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TRFFinalRegistry.MODID);

    public static final DeferredItem<Item> BASIC_STAFF_ITEM = ITEMS.registerItem("basic_staff", BasicStaffItem::new, AbstractStaffItem.STAFF_PROPERTIES);

    public static final DeferredItem<BlockItem> LIGHTS_OUT_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("lights_out_block", BlocksInit.LIGHTS_OUT_BLOCK);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
