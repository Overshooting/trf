package com.gmail.aamelis.trf.ModAttachments.QuestAttachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class PlayerQuestData {

    private final Map<ResourceLocation, QuestProgress> quests;

    public PlayerQuestData() {
        this(new HashMap<>());
    }

    public PlayerQuestData(Map<ResourceLocation, QuestProgress> quests) {
        this.quests = new HashMap<>(quests);
    }

    public QuestProgress getOrCreate(ResourceLocation questId) {
        return quests.computeIfAbsent(questId, k -> new QuestProgress());
    }

    public Map<ResourceLocation, QuestProgress> getAll() {
        return quests;
    }

    public static final MapCodec<PlayerQuestData> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.unboundedMap(ResourceLocation.CODEC, QuestProgress.CODEC.codec())
                            .fieldOf("quests")
                            .forGetter(data -> data.quests)
            ).apply(instance, PlayerQuestData::new)
    );
}
