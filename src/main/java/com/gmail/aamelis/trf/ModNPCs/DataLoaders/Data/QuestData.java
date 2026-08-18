package com.gmail.aamelis.trf.ModNPCs.DataLoaders.Data;

import java.util.List;

public record QuestData(boolean global, boolean repeatable, String completedBroadcast, List<StageData> stages) {
}
