package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModItems.Scrolls.AbstractScrollItem;
import com.gmail.aamelis.trf.ModItems.Scrolls.Mage.*;
import com.gmail.aamelis.trf.ModItems.Stats.SoulVial;
import com.gmail.aamelis.trf.ModItems.Weapons.Mage.AbstractStaffItem;
import com.gmail.aamelis.trf.ModItems.Weapons.Mage.BasicStaffItem;
import com.gmail.aamelis.trf.ModItems.Weapons.Ranger.AbstractModBowItem;
import com.gmail.aamelis.trf.ModItems.Weapons.Ranger.Arrows.CopperArrowItem;
import com.gmail.aamelis.trf.ModItems.Weapons.Ranger.SniperBowItem;
import com.gmail.aamelis.trf.ModItems.Weapons.Ranger.BasicBowItem;
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

    public static final DeferredItem<Item> MANA_BLAST_SCROLL_ITEM = ITEMS.registerItem("mana_blast_scroll", ManaBlastScroll::new, AbstractScrollItem.SCROLL_PROPERTIES);

    public static final DeferredItem<Item> PAINTED_PANTHEON_SCROLL_ITEM = ITEMS.registerItem("painted_pantheon_scroll", PaintedPantheonScroll::new, AbstractScrollItem.SCROLL_PROPERTIES);

    public static final DeferredItem<Item> SUNLIGHT_REACH_SCROLL_ITEM = ITEMS.registerItem("sunlight_reach_scroll", SunlightReachScroll::new, AbstractScrollItem.SCROLL_PROPERTIES);

    public static final DeferredItem<Item> HYACINTH_BLADES_SCROLL_ITEM = ITEMS.registerItem("hyacinth_blade_scroll", HyacinthBladeScroll::new, AbstractScrollItem.SCROLL_PROPERTIES);

    public static final DeferredItem<Item> SOUL_VIAL_ITEM = ITEMS.registerItem("soul_vial", SoulVial::new, SoulVial.PROPERTIES);

    public static final DeferredItem<Item> COPPER_ARROW_ITEM = ITEMS.registerItem("copper_arrow", CopperArrowItem::new, CopperArrowItem.PROPERTIES);

    public static final DeferredItem<Item> BASIC_BOW_ITEM = ITEMS.registerItem("basic_bow", BasicBowItem::new, AbstractModBowItem.PROPERTIES);

    public static final DeferredItem<Item> SNIPER_BOW_ITEM = ITEMS.registerItem("sniper_bow", SniperBowItem::new, AbstractModBowItem.PROPERTIES);

    public static final DeferredItem<BlockItem> LIGHTS_OUT_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("lights_out_block", BlocksInit.LIGHTS_OUT_BLOCK);

    public static final DeferredItem<BlockItem> GAME_MASTER_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("game_master_block", BlocksInit.GAME_MASTER_BLOCK);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
