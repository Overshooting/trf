package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModAdvancements.LightsOutSolvedTrigger;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AdvancementTriggersInit {

    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS =
            DeferredRegister.create(Registries.TRIGGER_TYPE, TRFFinalRegistry.MODID);

    public static final Supplier<LightsOutSolvedTrigger> LIGHTS_OUT_SOLVED_TRIGGER = TRIGGERS.register("lights_out_solved_trigger", LightsOutSolvedTrigger::new);

    public static void register(IEventBus eventBus) {
        TRIGGERS.register(eventBus);
    }

}
