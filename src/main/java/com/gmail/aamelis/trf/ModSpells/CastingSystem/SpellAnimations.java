package com.gmail.aamelis.trf.ModSpells.CastingSystem;

import com.gmail.aamelis.trf.TRFFinalRegistry;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.enums.PlayState;
import net.minecraft.resources.ResourceLocation;

public class SpellAnimations {

    public static final ResourceLocation SPELL_LAYER_ID =
            ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "spell_layer");

    public static void registerFactory() {
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                SPELL_LAYER_ID,
                1500,
                player ->
                    new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP)

        );
    }

}
