package com.gmail.aamelis.trf.ModNPCs.DataLoaders;

import com.gmail.aamelis.trf.ModNPCs.DataLoaders.Data.ItemRewardData;
import com.gmail.aamelis.trf.ModNPCs.DataLoaders.Data.ObjectiveData;
import com.gmail.aamelis.trf.ModNPCs.DataLoaders.Data.QuestData;
import com.gmail.aamelis.trf.ModNPCs.DataLoaders.Data.StageData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public class QuestCodecs {

    public static final Codec<ObjectiveData> OBJECTIVE_CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                Codec.STRING.fieldOf("type").forGetter(ObjectiveData::type),
                Codec.STRING.fieldOf("target").forGetter(ObjectiveData::target),
                Codec.INT.fieldOf("count").forGetter(ObjectiveData::count)
        ).apply(instance, ObjectiveData::new)
    );

    public static final Codec<ItemRewardData> ITEM_REWARD_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("id").forGetter(ItemRewardData::id),
                    Codec.INT.fieldOf("count").forGetter(ItemRewardData::count)
            ).apply(instance, ItemRewardData::new)
    );

    public static final Codec<StageData> STAGE_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                Codec.STRING.fieldOf("dialog").forGetter(StageData::dialog),
                    OBJECTIVE_CODEC.listOf().fieldOf("objectives").forGetter(StageData::objectives),
                    Codec.INT.fieldOf("experience").forGetter(StageData::experience),
                    ITEM_REWARD_CODEC.optionalFieldOf("item").forGetter(StageData::item)
            ).apply(instance, StageData::new)
    );

    public static final Codec<QuestData> QUEST_DATA_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BOOL.optionalFieldOf("global", false)
                            .forGetter(QuestData::global),
                    Codec.BOOL.optionalFieldOf("repeatable", false)
                                    .forGetter(QuestData::repeatable),
                    STAGE_CODEC.listOf()
                            .fieldOf("stages")
                            .forGetter(QuestData::stages)
            ).apply(instance, QuestData::new)
    );

}
