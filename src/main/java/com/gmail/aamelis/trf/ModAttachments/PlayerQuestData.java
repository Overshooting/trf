package com.gmail.aamelis.trf.ModAttachments;

import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.NPCName;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.Quests.QuestStep;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.*;

public class PlayerQuestData {

    private final Map<NPCName, QuestStep> ongoingQuests;

    public PlayerQuestData() {
        this(new HashMap<>());
    }

    public PlayerQuestData(Map<NPCName, QuestStep> map) {
        ongoingQuests = new HashMap<>(map);
    }

    public boolean hasQuest(NPCName name, QuestStep step) {
        return ongoingQuests.containsKey(name) && ongoingQuests.get(name).equals(step);
    }

    public boolean hasQuest(NPCName name) {
        return ongoingQuests.containsKey(name);
    }

    public boolean doingQuest(NPCName name, QuestStep step) {
        return hasQuest(name, step) && !ongoingQuests.get(name).isCompleted();
    }

    public boolean questFinished(NPCName name, QuestStep step) {
        return hasQuest(name, step) && ongoingQuests.get(name).isCompleted();
    }

    public boolean NPCQuestFinished(NPCName name) {
        return ongoingQuests.get(name).getStageDialog().equals("Completed!");
    }

    public void addQuest(NPCName name, QuestStep step) {
        ongoingQuests.put(name, step);
    }

    public void finishNPCQuest(NPCName name) {
        ongoingQuests.replace(name, new QuestStep("Completed!"));
    }

    public void updateNPCQuest(NPCName name, QuestStep step) {
        ongoingQuests.replace(name, step);
    }

    public static final MapCodec<PlayerQuestData> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.unboundedMap(NPCName.CODEC, QuestStep.CODEC)
                            .fieldOf("quests")
                            .forGetter(data -> data.ongoingQuests)
            ).apply(instance, PlayerQuestData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerQuestData> STREAM_CODEC =
            StreamCodec.of(
                    (buf, data) -> {
                        buf.writeInt(data.ongoingQuests.size());

                        for (var entry : data.ongoingQuests.entrySet()) {
                            NPCName.STREAM_CODEC.encode(buf, entry.getKey());
                            QuestStep.STREAM_CODEC.encode(buf, entry.getValue());
                        }
                    },
                    buf -> {
                        int size = buf.readInt();
                        HashMap<NPCName, QuestStep> map = new HashMap<>();

                        for (int i = 0; i < size; i++) {
                            NPCName name = NPCName.STREAM_CODEC.decode(buf);
                            QuestStep step = QuestStep.STREAM_CODEC.decode(buf);
                            map.put(name, step);
                        }

                        return new PlayerQuestData(map);
                    }
            );
}
