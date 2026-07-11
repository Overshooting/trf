package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SoundsInit {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(
            Registries.SOUND_EVENT, TRFFinalRegistry.MODID
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> MONSTER_TRAP_PLACE = registerSoundEvent("monster_trap_place");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, name)));
    }

    public static void register(IEventBus bus) {
        SOUNDS.register(bus);
    }

}
