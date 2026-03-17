package com.gmail.aamelis.trf_gi.Registries;

import com.gmail.aamelis.trf_gi.ModAttachments.PlayerSpellData;
import com.gmail.aamelis.trf_gi.TRFGearAndItemsFinalRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AttachmentTypesInit {

    public static DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, TRFGearAndItemsFinalRegistry.MODID);

    public static final Supplier<AttachmentType<PlayerSpellData>> PLAYER_SPELL_DATA =
            ATTACHMENT_TYPES.register("player_spell_data",
                    () -> AttachmentType.builder(PlayerSpellData::new)
                            .serialize(PlayerSpellData.CODEC)
                            .copyOnDeath()
                            .build());

    public static void register(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
    }
}
