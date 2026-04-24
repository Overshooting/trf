package com.gmail.aamelis.trf.ModNPCs.DataLoaders.Data;

import java.util.List;
import java.util.Optional;

public record StageData(String dialog, List<ObjectiveData> objectives, int experience, Optional<ItemRewardData> item) {
}
