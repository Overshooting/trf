package com.gmail.aamelis.trf.ModNPCs.DataLoaders;

import com.gmail.aamelis.trf.ModNPCs.DataLoaders.Data.ObjectiveData;
import com.gmail.aamelis.trf.ModNPCs.DataLoaders.Data.StageData;
import com.gmail.aamelis.trf.ModNPCs.Quests.Objectives.ItemObjective;
import com.gmail.aamelis.trf.ModNPCs.Quests.Objectives.KillObjective;
import com.gmail.aamelis.trf.ModNPCs.Quests.Objectives.QuestObjective;
import com.gmail.aamelis.trf.ModNPCs.Quests.Objectives.TriggerObjective;
import com.gmail.aamelis.trf.ModNPCs.Quests.QuestLine;
import com.gmail.aamelis.trf.ModNPCs.Quests.QuestStage;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class QuestDataLoader extends SimpleJsonResourceReloadListener<List<StageData>> {

    public static final Map<ResourceLocation, QuestLine> LOADED_QUESTS = new HashMap<>();

    public QuestDataLoader() {
        super(QuestCodecs.QUEST_CODEC, FileToIdConverter.json("quests"));
    }

    @Override
    protected void apply(Map<ResourceLocation, List<StageData>> data, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        LOADED_QUESTS.clear();

        for (Map.Entry<ResourceLocation, List<StageData>> entry : data.entrySet()) {
            ResourceLocation id = entry.getKey();
            List<StageData> stagesRaw = entry.getValue();

            List<QuestStage> stages = stagesRaw.stream()
                    .map(this::convertStage)
                    .toList();

            LOADED_QUESTS.put(id, new QuestLine(id, stages));
        }
    }

    private QuestStage convertStage(StageData data) {
        ItemStack rewardStack = null;

        if (data.item().isPresent()) {
            var itemData = data.item().get();

            var item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemData.id()));

            if (item.isEmpty()) {
                throw new IllegalStateException("Invalid Item in reward: " + itemData.id());
            }

            rewardStack = new ItemStack(item.get().value(), itemData.count());
        }

        return new QuestStage(data.dialog(), data.objectives().stream().map(this::convertObjective).toList(), data.experience(), rewardStack);
    }

    private QuestObjective convertObjective(ObjectiveData data) {
        return switch(data.type()) {
            case "kill" -> {
                var type = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(data.target()));

                if (type.isEmpty()) {
                    throw new IllegalStateException("Error loading entity type of type: " + data.target());
                }

                yield new KillObjective(type.get().value(), data.count());
            }

            case "item" -> {
                var item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(data.target()));

                if (item.isEmpty()) {
                    throw new IllegalStateException("Error loading item of type: " + data.target());
                }

                yield new ItemObjective(item.get().value(), data.count());
            }

            case "custom" -> new TriggerObjective(data.target(), data.count());

            default -> throw new IllegalStateException("Invalid objective type found when loading quests: " + data.type());
        };
    }
}
