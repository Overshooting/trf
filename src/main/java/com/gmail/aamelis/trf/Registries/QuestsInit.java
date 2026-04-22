package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModAttachments.QuestAttachments.PlayerQuestData;
import com.gmail.aamelis.trf.ModAttachments.QuestAttachments.QuestProgress;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.DataLoaders.QuestDataLoader;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.NPCName;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.Quests.Objectives.QuestObjective;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.Quests.QuestLine;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.Quests.QuestStage;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class QuestsInit {

    public static final Map<NPCName, ResourceLocation> NPC_TO_QUEST = new HashMap<>();

    public static QuestLine getQuest(NPCName name) {
        if (name == null) {
            throw new IllegalArgumentException("Illegal npc questline requested!");
        }

        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, name.getName().toLowerCase());

        QuestLine quest = QuestDataLoader.LOADED_QUESTS.getOrDefault(id, null);

        if (quest == null) {
            throw new IllegalStateException("Missing quest JSON for NPC: " + name.getName() + " (" + id + ")");
        }

        return quest;
    }

    public static QuestLine getQuest(ResourceLocation id) {
        QuestLine quest = QuestDataLoader.LOADED_QUESTS.get(id);

        if (quest == null) {
            throw new IllegalStateException("Quest not found for id: " + id);
        }

        return quest;
    }

    public static void forEachActiveObjective(ServerPlayer player, BiConsumer<QuestObjective, QuestProgress> action) {
        PlayerQuestData data = player.getData(AttachmentTypesInit.PLAYER_QUEST_DATA);

        for (var entry: data.getAll().entrySet()) {
            QuestLine questLine = QuestsInit.getQuest(entry.getKey());
            QuestProgress progress = entry.getValue();

            int stageIndex = progress.getStage();
            if (stageIndex >= questLine.stages().size()) continue;

            QuestStage stage = questLine.stages().get(stageIndex);

            for (QuestObjective obj : stage.objectives()) {
                action.accept(obj, progress);
            }
        }
    }

}
