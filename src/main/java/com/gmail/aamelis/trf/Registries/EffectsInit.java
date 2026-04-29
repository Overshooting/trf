package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModEffects.ShockedEffect;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EffectsInit {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(
            BuiltInRegistries.MOB_EFFECT, TRFFinalRegistry.MODID
    );

    public static final DeferredHolder<MobEffect, ShockedEffect> SHOCKED_EFFECT = MOB_EFFECTS.register("shocked_effect", () ->
            new ShockedEffect(MobEffectCategory.HARMFUL, 0x00F0FF));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }

}
