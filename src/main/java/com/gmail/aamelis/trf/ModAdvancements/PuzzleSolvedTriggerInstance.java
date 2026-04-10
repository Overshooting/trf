package com.gmail.aamelis.trf.ModAdvancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;

import java.util.Optional;

public record PuzzleSolvedTriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {

    public static final Codec<PuzzleSolvedTriggerInstance> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player")
                            .forGetter(PuzzleSolvedTriggerInstance::player)
            ).apply(instance, PuzzleSolvedTriggerInstance::new));

    public boolean matches() {
        return true;
    }

}
