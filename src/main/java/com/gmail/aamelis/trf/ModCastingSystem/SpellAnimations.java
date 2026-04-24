package com.gmail.aamelis.trf.ModCastingSystem;

import com.gmail.aamelis.trf.TRFFinalRegistry;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonConfiguration;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonMode;
import com.zigythebird.playeranimcore.enums.PlayState;
import net.minecraft.resources.ResourceLocation;

public class SpellAnimations {

    public static final ResourceLocation SPELL_LAYER_ID =
            ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "spell_layer");

    public static void registerFactory() {
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                SPELL_LAYER_ID,
                1500,
                player -> {
                    PlayerAnimationController controller = new PlayerAnimationController(player, (controller1, state, animSetter) -> PlayState.STOP);

                    controller.setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);
                    controller.setFirstPersonConfiguration(new FirstPersonConfiguration()
                            .setShowLeftArm(true)
                            .setShowRightArm(true)
                            .setShowRightItem(true)
                            .setShowLeftItem(true));

                    return controller;
                }

        );
    }

}
