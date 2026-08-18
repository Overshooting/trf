package com.gmail.aamelis.trf.ModNPCs.NPCsData;

import com.gmail.aamelis.trf.ModNPCs.Quests.QuestLine;
import com.gmail.aamelis.trf.Registries.QuestsInit;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.gmail.aamelis.trf.ModNPCs.NPCsData.NPCType.*;

public enum NPCName {
    DEFAULT("null", FLAVOR_TYPE),
    WILLIAM("William", FLAVOR_TYPE),
    ALEX("Alex", FLAVOR_TYPE),
    HEAD_GENERAL("Head General", TUTORIAL_TYPE),
    GRASS_EATER("Grass Eater", STEP_QUEST_TYPE);

    private final String name;
    private final byte type;
    private static final HashMap<String, NPCName> nameMap = new HashMap<>();
    private static final ArrayList<String> validFlavorNames = new ArrayList<>();
    private static final ArrayList<String> validQuestNames = new ArrayList<>();
    private static final ArrayList<String> validTutorialNames = new ArrayList<>();

    static {
        for (NPCName thisName : NPCName.values()) {
            nameMap.put(thisName.name, thisName);

            switch (thisName.type) {
                case FLAVOR_TYPE -> validFlavorNames.add(thisName.name);
                case STEP_QUEST_TYPE -> validQuestNames.add(thisName.name);
                case TUTORIAL_TYPE -> validTutorialNames.add(thisName.name);

                default -> throw new IllegalStateException("Illegal NPC type: " + thisName.type + " for NPC name: " + thisName.name + " found during loading!");
            }
        }
    }

    NPCName(String name, byte type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public byte getType() {
        return type;
    }

    public String getResourceLocationName() {
        return name.toLowerCase().replaceAll("[ ,]+", "_");
    }

    public static NPCName matchNameOrDefault(String nameCheck, NPCName defaultName) {
        return nameMap.getOrDefault(nameCheck, defaultName);
    }

    public static ArrayList<String> getValidFlavorNames() {
        return validFlavorNames;
    }

    public static ArrayList<String> getValidQuestNames() {
        return validQuestNames;
    }

    public static ArrayList<String> getValidTutorialNames() {
        return validTutorialNames;
    }

    public static NPCName matchNameFromString(String lowercase) {
        for (NPCName name : NPCName.values()) {
            if (name.getName().toLowerCase().equals(lowercase)) {
                return name;
            }
        }
        return null;
    }

    public static List<String> getGlobalQuestIds() {
        List<String> globalIds = new ArrayList<>();
        for (String name : validQuestNames) {
            String resourceLocationName = name.toLowerCase().replaceAll("[ ,]+", "_");
            ResourceLocation questId = ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, resourceLocationName);

            QuestLine line = QuestsInit.getQuest(questId);

            if (line.isGlobal()) {
                globalIds.add(resourceLocationName);
            }
        }

        for (String name : validTutorialNames) {
            String resourceLocationName = name.toLowerCase().replaceAll("[ ,]+", "_");
            ResourceLocation questId = ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, resourceLocationName);

            QuestLine line = QuestsInit.getQuest(questId);

            if (line.isGlobal()) {
                globalIds.add(resourceLocationName);
            }
        }

        return globalIds;
    }

    public static List<String> getPlayerQuestIds() {
        List<String> playerIds = new ArrayList<>();
        for (String name : validQuestNames) {
            String resourceLocationName = name.toLowerCase().replaceAll("[ ,]+", "_");
            ResourceLocation questId = ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, resourceLocationName);

            QuestLine line = QuestsInit.getQuest(questId);

            if (!line.isGlobal()) {
                playerIds.add(resourceLocationName);
            }
        }

        for (String name : validTutorialNames) {
            String resourceLocationName = name.toLowerCase().replaceAll("[ ,]+", "_");
            ResourceLocation questId = ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, resourceLocationName);

            QuestLine line = QuestsInit.getQuest(questId);

            if (!line.isGlobal()) {
                playerIds.add(resourceLocationName);
            }
        }

        return playerIds;
    }

    public static final Codec<NPCName> CODEC = Codec.STRING.xmap(
            name -> NPCName.matchNameOrDefault(name, NPCName.DEFAULT),
            NPCName::getName
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, NPCName> STREAM_CODEC =
            StreamCodec.of(
                    (buf, npcName) -> buf.writeUtf(npcName.getName()),
                    buf -> NPCName.matchNameOrDefault(buf.readUtf(), NPCName.DEFAULT)
            );
}
