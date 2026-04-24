package com.gmail.aamelis.trf.ModNPCs.Quests;

import com.gmail.aamelis.trf.ModNPCs.Quests.Objectives.QuestObjective;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public record QuestStage(String dialog, List<QuestObjective> objectives, int experience, @Nullable ItemStack rewardItem) {
}
