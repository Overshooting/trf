package com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.Quests;

import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.Quests.Objectives.KillObjective;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public class TutorialQuestLines {

    public static QuestStage[] HEAD_GENERAL_QUESTLINE = {
                    new QuestStage("I need to test your mettle.\nKill 5 Zombies", List.of(new KillObjective(EntityType.ZOMBIE, 5))),
                    new QuestStage("Done.", List.of()),
                    new QuestStage("Scram!", List.of())
    };

}
