package com.gmail.aamelis.trf.ModNPCs;

import com.gmail.aamelis.trf.ModAttachments.PlayerSpellData;
import com.gmail.aamelis.trf.ModNPCs.NPCsData.NPCArea;
import com.gmail.aamelis.trf.ModNPCs.NPCsData.NPCName;
import com.gmail.aamelis.trf.ModEntities.NPCs.Types.FlavorNPCEntity;
import com.gmail.aamelis.trf.ModEntities.NPCs.Types.StepQuestNPCEntity;
import com.gmail.aamelis.trf.ModEntities.NPCs.Types.TutorialStepQuestNPCEntity;
import com.gmail.aamelis.trf.Registries.EntitiesInit;
import net.minecraft.world.level.Level;

public class NPCConstructionHandler {

    public static FlavorNPCEntity buildFlavorNPC(NPCArea area, NPCName name, Level level) {
        FlavorNPCEntity thisEntity = new FlavorNPCEntity(EntitiesInit.FLAVOR_NPC_ENTITY.get(), level);
        thisEntity.setLocation(area);
        thisEntity.setName(name);

        return thisEntity;
    }

    public static StepQuestNPCEntity buildStepQuestNPC(NPCName name, Level level) {
        StepQuestNPCEntity thisEntity = new StepQuestNPCEntity(EntitiesInit.STEP_QUEST_NPC_ENTITY.get(), level);

        thisEntity.setName(name);

        return thisEntity;
    }

    public static TutorialStepQuestNPCEntity buildTutorialStepQuestNPC(NPCName name, Level level) {
        TutorialStepQuestNPCEntity thisEntity = new TutorialStepQuestNPCEntity(EntitiesInit.TUTORIAL_STEP_QUEST_NPC_ENTITY.get(), level);

        switch (name) {
            case HEAD_GENERAL -> thisEntity.setRequiredClass(PlayerSpellData.WARRIOR);
        }

        thisEntity.setName(name);

        return thisEntity;
    }

}
