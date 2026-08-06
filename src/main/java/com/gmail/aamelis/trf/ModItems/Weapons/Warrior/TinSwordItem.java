package com.gmail.aamelis.trf.ModItems.Weapons.Warrior;

import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

import java.util.function.Supplier;

public class TinSwordItem extends AbstractMeleeItem {

    public static final Supplier<Properties> PROPERTIES = () -> new Item.Properties()
            .sword(ToolMaterial.COPPER, 2.5f, 1.0f)
            .stacksTo(1);

    public TinSwordItem(Properties properties) {
        super(properties);
    }

    @Override
    ResourceLocation animId() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "animation.player.sword_parry");
    }
}
