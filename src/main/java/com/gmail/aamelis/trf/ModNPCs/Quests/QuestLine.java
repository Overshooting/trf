package com.gmail.aamelis.trf.ModNPCs.Quests;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record QuestLine(ResourceLocation id, boolean isGlobal, List<QuestStage> stages) {
}
