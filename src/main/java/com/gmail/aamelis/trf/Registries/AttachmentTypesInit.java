package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModItems.DataComponents.BowCastingData;
import com.gmail.aamelis.trf.ModPlayerData.ModStats.Levels.PlayerLevelData;
import com.gmail.aamelis.trf.ModPlayerData.ModStats.PlayerStatData;
import com.gmail.aamelis.trf.ModPlayerData.PlayerMana;
import com.gmail.aamelis.trf.ModPlayerData.QuestPlayerData.PlayerQuestData;
import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
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
                            .sync(PlayerSpellData.STREAM_CODEC)
                            .build());

    public static final Supplier<AttachmentType<PlayerMana>> PLAYER_MANA =
            ATTACHMENT_TYPES.register("player_mana",
                    () -> AttachmentType.builder(PlayerMana::new)
                            .serialize(PlayerMana.CODEC)
                            .copyOnDeath()
                            .sync(PlayerMana.STREAM_CODEC)
                            .build());

    public static final Supplier<AttachmentType<PlayerQuestData>> PLAYER_QUEST_DATA =
            ATTACHMENT_TYPES.register("player_quest_data",
                    () -> AttachmentType.builder(() -> new PlayerQuestData())
                            .serialize(PlayerQuestData.CODEC)
                            .copyOnDeath()
                            .build());

    public static final Supplier<AttachmentType<PlayerStatData>> PLAYER_STATS =
            ATTACHMENT_TYPES.register("player_stats",
                    () -> AttachmentType.builder(PlayerStatData::new)
                            .serialize(PlayerStatData.CODEC)
                            .copyOnDeath()
                            .sync(PlayerStatData.STREAM_CODEC)
                            .build());

    public static final Supplier<AttachmentType<PlayerLevelData>> PLAYER_LEVEL =
            ATTACHMENT_TYPES.register("player_level",
                    () -> AttachmentType.builder(PlayerLevelData::new)
                            .serialize(PlayerLevelData.CODEC)
                            .copyOnDeath()
                            .sync(PlayerLevelData.STREAM_CODEC)
                            .build());

    public static void register(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
    }
}
