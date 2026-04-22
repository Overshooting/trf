package com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Mob;

import java.util.ArrayList;
import java.util.HashMap;

public enum NPCName {
    DEFAULT("null"),
    WILLIAM("William"),
    ALEX("Alex"),
    HEAD_GENERAL("Head General");

    private final String name;
    private static final HashMap<String, NPCName> nameMap = new HashMap<>();
    private static final ArrayList<String> validFlavorNames = new ArrayList<>();
    private static final ArrayList<String> validQuestNames = new ArrayList<>();
    private static final ArrayList<String> validTutorialNames = new ArrayList<>();

    static {
        for (NPCName thisName : NPCName.values()) {
            nameMap.put(thisName.name, thisName);
        }

        validFlavorNames.add(WILLIAM.name);
        validFlavorNames.add(ALEX.name);

        validTutorialNames.add(HEAD_GENERAL.name);
    }

    NPCName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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
