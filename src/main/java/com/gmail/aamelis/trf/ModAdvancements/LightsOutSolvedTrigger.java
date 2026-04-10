package com.gmail.aamelis.trf.ModAdvancements;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

public class LightsOutSolvedTrigger extends SimpleCriterionTrigger<PuzzleSolvedTriggerInstance> {

    public void trigger(ServerPlayer player) {
        this.trigger(player, PuzzleSolvedTriggerInstance::matches);
    }

    @Override
    public Codec<PuzzleSolvedTriggerInstance> codec() {
        return PuzzleSolvedTriggerInstance.CODEC;
    }
}
