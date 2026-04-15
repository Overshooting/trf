package com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData;

import com.gmail.aamelis.trf.ModAttachments.PlayerSpellData;
import com.gmail.aamelis.trf.ModEntities.NPCs.FlavorNPCEntity;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.Quests.QuestStep;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.Quests.TutorialQuestTrees;
import com.gmail.aamelis.trf.ModEntities.NPCs.StepQuestNPCEntity;
import com.gmail.aamelis.trf.ModEntities.NPCs.TutorialStepQuestNPCEntity;
import com.gmail.aamelis.trf.Registries.EntitiesInit;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class NPCConstructionHandler {

    public static final HashMap<NPCName, QuestStep[]> NAME_TO_QUEST_MAP = new HashMap<>(Map.ofEntries(
            Map.entry(NPCName.HEAD_GENERAL, TutorialQuestTrees.HEAD_GENERAL_QUEST_TREE)
    ));

    public static FlavorNPCEntity buildFlavorNPC(NPCArea area, NPCName name, Level level) {
        FlavorNPCEntity thisEntity = new FlavorNPCEntity(EntitiesInit.FLAVOR_NPC_ENTITY.get(), level);
        thisEntity.setLocation(area);
        thisEntity.setName(name);

        return thisEntity;
    }

    public static StepQuestNPCEntity buildStepQuestNPC(NPCName name, Level level) {
        StepQuestNPCEntity thisEntity = new StepQuestNPCEntity(EntitiesInit.STEP_QUEST_NPC_ENTITY.get(), level);

        thisEntity.setNameAndQuests(name, NAME_TO_QUEST_MAP.getOrDefault(name, new QuestStep[0]));

        return thisEntity;
    }

    public static TutorialStepQuestNPCEntity buildTutorialStepQuestNPC(NPCName name, Level level) {
        TutorialStepQuestNPCEntity thisEntity = new TutorialStepQuestNPCEntity(EntitiesInit.TUTORIAL_STEP_QUEST_NPC_ENTITY.get(), level);

        thisEntity.setNameAndQuests(name, NAME_TO_QUEST_MAP.getOrDefault(name, new QuestStep[0]));
        switch (name) {
            case HEAD_GENERAL -> thisEntity.setRequiredClass(PlayerSpellData.WARRIOR);
        }

        return thisEntity;
    }

}
