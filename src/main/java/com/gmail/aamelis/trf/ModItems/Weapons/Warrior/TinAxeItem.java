package com.gmail.aamelis.trf.ModItems.Weapons.Warrior;

import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

import java.util.function.Supplier;

public class TinAxeItem extends AbstractMeleeItem {

    public static final Supplier<Properties> PROPERTIES = () -> new Item.Properties()
            .sword(ToolMaterial.COPPER, 3.0f, 1.25f)
            .stacksTo(1);

    public TinAxeItem(Properties properties) {
        super(properties, 20, SwordType.AXE_TYPE);
    }

    @Override
    ResourceLocation animId() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "animation.player.axe_parry");
    }
}
