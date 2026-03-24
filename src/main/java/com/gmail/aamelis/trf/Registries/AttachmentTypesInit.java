package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModAttachments.PlayerMana;
import com.gmail.aamelis.trf.ModAttachments.PlayerSpellData;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AttachmentTypesInit {

    public static DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, TRFFinalRegistry.MODID);

    public static final Supplier<AttachmentType<PlayerSpellData>> PLAYER_SPELL_DATA =
            ATTACHMENT_TYPES.register("player_spell_data",
                    () -> AttachmentType.builder(PlayerSpellData::new)
                            .serialize(PlayerSpellData.CODEC)
                            .copyOnDeath()
                            .build());

    public static final Supplier<AttachmentType<PlayerMana>> PLAYER_MANA =
            ATTACHMENT_TYPES.register("player_mana",
                    () -> AttachmentType.builder(PlayerMana::new)
                            .serialize(PlayerMana.CODEC)
                            .copyOnDeath()
                            .sync(PlayerMana.STREAM_CODEC)
                            .build());

    public static void register(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
    }
}
