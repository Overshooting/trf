package com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.Quests;

import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.Quests.Objectives.QuestObjective;

import java.util.List;

public record QuestStage(String dialog, List<QuestObjective> objectives) {
}
