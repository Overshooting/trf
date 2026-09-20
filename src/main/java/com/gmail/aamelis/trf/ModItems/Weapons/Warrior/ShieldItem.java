package com.gmail.aamelis.trf.ModItems.Weapons.Warrior;

import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

import java.util.function.Supplier;

public class ShieldItem extends AbstractMeleeItem {

    public static final Supplier<Properties> PROPERTIES = () -> new Item.Properties()
            .sword(ToolMaterial.NETHERITE, 60.0f, 0.2f)
            .stacksTo(1);

    public ShieldItem(Properties properties) {
        super(properties, 60, SwordType.SWORD_TYPE);
    }

    @Override
    ResourceLocation animId() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "animation.player.sword_parry");
    }
}
