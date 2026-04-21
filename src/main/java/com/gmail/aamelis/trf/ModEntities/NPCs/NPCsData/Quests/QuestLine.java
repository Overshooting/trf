package com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.Quests;

import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.NPCName;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record QuestLine(ResourceLocation id, List<QuestStage> stages) {
}
