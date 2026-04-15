package com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.Quests;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public class QuestStep implements ValueIOSerializable {

    private String stageDialog;
    private boolean completed;

    public QuestStep() {
        stageDialog = "Lorem Ipsum";
    }

    public QuestStep(String thisStageDialog) {
        stageDialog = thisStageDialog;
        completed = false;
    }

    public QuestStep(String thisStageDialog, boolean isCompleted) {
        stageDialog = thisStageDialog;
        completed = isCompleted;
    }

    public String getStageDialog() {
        return stageDialog;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof QuestStep other)) return false;
        return stageDialog.equals(other.stageDialog);
    }

    public boolean isCompleted() {
        return completed;
    }

    @Override
    public void serialize(ValueOutput valueOutput) {
        valueOutput.putString("dialog", stageDialog);
        valueOutput.putBoolean("completed", completed);
    }

    @Override
    public void deserialize(ValueInput valueInput) {
        stageDialog = valueInput.getStringOr("dialog", "Dialog Corrupted!");
        completed = valueInput.getBooleanOr("completed", false);
    }

    public static final Codec<QuestStep> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("dialog").forGetter(QuestStep::getStageDialog),
                    Codec.BOOL.fieldOf("completed").forGetter(QuestStep::isCompleted)
            ).apply(instance, QuestStep::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, QuestStep> STREAM_CODEC =
            StreamCodec.of(
                    (buf, step) -> {
                        buf.writeUtf(step.stageDialog);
                        buf.writeBoolean(step.completed);
                    },
                    buf -> new QuestStep(buf.readUtf(), buf.readBoolean())
            );

}
