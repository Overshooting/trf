package com.gmail.aamelis.trf.ModItems.Weapons.Warrior;

import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.resources.ResourceLocation;

public class TinDaggerItem extends AbstractMeleeItem {

    public TinDaggerItem(Properties properties) {
        super(properties, 10, SwordType.DAGGER_TYPE);
    }

    @Override
    ResourceLocation animId() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "animation.player.dagger_parry");
    }
}
