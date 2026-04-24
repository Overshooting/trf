package com.gmail.aamelis.trf.ModNPCs.NPCsData;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.HashMap;

import static com.gmail.aamelis.trf.ModNPCs.NPCsData.NPCType.*;

public enum NPCName {
    DEFAULT("null", FLAVOR_TYPE),
    WILLIAM("William", FLAVOR_TYPE),
    ALEX("Alex", FLAVOR_TYPE),
    HEAD_GENERAL("Head General", TUTORIAL_TYPE);

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
        return name.toLowerCase().replace(" ", "_");
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
