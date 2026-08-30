package com.gmail.aamelis.trf.ModNPCs.DataLoaders;

import com.gmail.aamelis.trf.ModNPCs.DataLoaders.Data.ItemRewardData;
import com.gmail.aamelis.trf.ModNPCs.DataLoaders.Data.ObjectiveData;
import com.gmail.aamelis.trf.ModNPCs.DataLoaders.Data.QuestData;
import com.gmail.aamelis.trf.ModNPCs.DataLoaders.Data.StageData;
import com.gmail.aamelis.trf.ModNPCs.Quests.Objectives.ItemObjective;
import com.gmail.aamelis.trf.ModNPCs.Quests.Objectives.KillObjective;
import com.gmail.aamelis.trf.ModNPCs.Quests.Objectives.QuestObjective;
import com.gmail.aamelis.trf.ModNPCs.Quests.Objectives.TriggerObjective;
import com.gmail.aamelis.trf.ModNPCs.Quests.QuestStage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

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

    public static final Codec<StageData> STAGE_DATA_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                Codec.STRING.fieldOf("dialog").forGetter(StageData::dialog),
                    OBJECTIVE_CODEC.listOf().fieldOf("objectives").forGetter(StageData::objectives),
                    Codec.INT.fieldOf("experience").forGetter(StageData::experience),
                    ITEM_REWARD_CODEC.optionalFieldOf("item").forGetter(StageData::item),
                    Codec.STRING.optionalFieldOf("broadcast").forGetter(StageData::broadcast)
            ).apply(instance, StageData::new)
    );

    public static final MapCodec<ItemObjective> ITEM_OBJECTIVE_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BuiltInRegistries.ITEM
                            .byNameCodec()
                            .fieldOf("item")
                            .forGetter(objective -> objective.getRequiredStack().getItem()),

                    Codec.INT
                            .fieldOf("count")
                            .forGetter(objective -> objective.getRequiredStack().getCount())

            ).apply(instance, ItemObjective::new)
    );

    public static final MapCodec<KillObjective> KILL_OBJECTIVE_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BuiltInRegistries.ENTITY_TYPE
                            .byNameCodec()
                            .fieldOf("target")
                            .forGetter(KillObjective::getTarget),

                    Codec.INT
                            .fieldOf("required")
                            .forGetter(KillObjective::getRequired)

            ).apply(instance, KillObjective::new)
    );

    public static final MapCodec<TriggerObjective> TRIGGER_OBJECTIVE_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING
                            .fieldOf("triggerId")
                            .forGetter(TriggerObjective::getTriggerId),

                    Codec.INT
                            .fieldOf("required")
                            .forGetter(TriggerObjective::getRequired)

            ).apply(instance, TriggerObjective::new)
    );

    public static final Codec<QuestObjective> QUEST_OBJECTIVE_CODEC =
            Codec.STRING.dispatch(
                    QuestObjective::type,
                    type -> switch (type) {
                        case "item" -> ITEM_OBJECTIVE_CODEC;
                        case "kill" -> KILL_OBJECTIVE_CODEC;
                        case "trigger" -> TRIGGER_OBJECTIVE_CODEC;
                        default -> throw new IllegalArgumentException(
                                "Unknown quest objective type: " + type
                        );
                    }
            );

    public static final MapCodec<Item> ITEM_NAME_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.INT.fieldOf("id")
                            .forGetter(Item::getId)
            ).apply(instance, Item::byId)
    );

    public static final MapCodec<QuestStage> STAGE_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.STRING
                            .fieldOf("dialog")
                            .forGetter(QuestStage::dialog),

                    QUEST_OBJECTIVE_CODEC
                            .listOf()
                            .fieldOf("objectives")
                            .forGetter(QuestStage::objectives),

                    Codec.INT
                            .fieldOf("experience")
                            .forGetter(QuestStage::experience),

                    ItemStack.CODEC
                            .optionalFieldOf("rewardItem")
                            .forGetter(stage -> Optional.ofNullable(stage.rewardItem())),

                    Codec.STRING
                            .optionalFieldOf("broadcast")
                            .forGetter(stage -> Optional.ofNullable(stage.broadcast()))

            ).apply(instance, (dialog, objectives, experience, rewardItem, broadcast) ->
                    new QuestStage(
                            dialog,
                            objectives,
                            experience,
                            rewardItem.orElse(null),
                            broadcast.orElse("")
                    )
            )
    );

    public static final Codec<QuestData> QUEST_DATA_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BOOL.optionalFieldOf("global", false)
                            .forGetter(QuestData::global),
                    Codec.BOOL.optionalFieldOf("repeatable", false)
                                    .forGetter(QuestData::repeatable),
                    Codec.STRING.optionalFieldOf("broadcast", "none")
                                    .forGetter(QuestData::completedBroadcast),
                    STAGE_DATA_CODEC.listOf()
                            .fieldOf("stages")
                            .forGetter(QuestData::stages)
            ).apply(instance, QuestData::new)
    );

}
