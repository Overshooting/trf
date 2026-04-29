package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModItems.Scrolls.AbstractScrollItem;
import com.gmail.aamelis.trf.ModItems.Scrolls.Mage.DispelScroll;
import com.gmail.aamelis.trf.ModItems.Scrolls.Mage.LightningBeamScroll;
import com.gmail.aamelis.trf.ModItems.Scrolls.Mage.ShadowStepScroll;
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

    public static final DeferredItem<Item> DISPEL_SCROLL_ITEM = ITEMS.registerItem("dispel_scroll", DispelScroll::new, AbstractScrollItem.SCROLL_PROPERTIES);

    public static final DeferredItem<Item> SHADOW_STEP_SCROLL_ITEM = ITEMS.registerItem("shadow_step_scroll", ShadowStepScroll::new, AbstractScrollItem.SCROLL_PROPERTIES);

    public static final DeferredItem<Item> LIGHTNING_BEAM_SCROLL_ITEM = ITEMS.registerItem("lightning_beam_scroll", LightningBeamScroll::new, AbstractScrollItem.SCROLL_PROPERTIES);

    public static final DeferredItem<BlockItem> LIGHTS_OUT_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("lights_out_block", BlocksInit.LIGHTS_OUT_BLOCK);

    public static final DeferredItem<BlockItem> GAME_MASTER_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("game_master_block", BlocksInit.GAME_MASTER_BLOCK);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
