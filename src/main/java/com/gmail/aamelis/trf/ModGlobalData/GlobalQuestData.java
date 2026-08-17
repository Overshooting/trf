package com.gmail.aamelis.trf.ModGlobalData;

import com.gmail.aamelis.trf.ModPlayerData.QuestPlayerData.QuestProgress;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.Map;

public class GlobalQuestData extends SavedData {

    private static final String DATA_NAME = "trf_global_quest_data";

    public static final MapCodec<GlobalQuestData> MAP_CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            Codec.unboundedMap(
                                            ResourceLocation.CODEC,
                                            QuestProgress.CODEC.codec()
                                    )
                                    .fieldOf("quests")
                                    .forGetter(data -> data.questProgress)
                    ).apply(instance, GlobalQuestData::new)
            );

    public static final Codec<GlobalQuestData> CODEC = MAP_CODEC.codec();

    public static final SavedDataType<GlobalQuestData> TYPE =
            new SavedDataType<GlobalQuestData>(
                    DATA_NAME,
                    GlobalQuestData::new,
                    CODEC,
                    null
            );

    private final HashMap<ResourceLocation, QuestProgress> questProgress;

    public GlobalQuestData() {
        this(new HashMap<>());
    }

    public GlobalQuestData(Map<ResourceLocation, QuestProgress> questProgress) {
        this.questProgress = new HashMap<>(questProgress);
    }

    public static GlobalQuestData getGlobalQuestData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(TYPE);
    }

    public QuestProgress getQuestProgress(ResourceLocation id) {
        return questProgress.computeIfAbsent(id, questId -> {
            setDirty();
            return new QuestProgress();
        });
    }

    public Map<ResourceLocation, QuestProgress> getAllQuestProgress() {
        return questProgress;
    }

    public void wipeAllQuestProgress() {
        questProgress.clear();
        setDirty();
    }

    public void wipeQuestProgress(ResourceLocation id) {
        questProgress.remove(id);
        setDirty();
    }

}
