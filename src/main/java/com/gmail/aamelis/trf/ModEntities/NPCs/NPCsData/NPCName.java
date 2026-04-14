package com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData;

import java.util.ArrayList;
import java.util.HashMap;

public enum NPCName {
    DEFAULT("null"),
    WILLIAM("William"),
    ALEX("Alex");

    private final String name;
    private static final HashMap<String, NPCName> nameMap = new HashMap<>();
    private static final ArrayList<String> validNames = new ArrayList<>();

    static {
        for (NPCName thisName : NPCName.values()) {
            nameMap.put(thisName.name, thisName);
            if (thisName != DEFAULT) {
                validNames.add(thisName.name);
            }
        }
    }

    NPCName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getResourceLocationName() {
        return name.toLowerCase();
    }

    public static NPCName matchNameOrDefault(String nameCheck, NPCName defaultName) {
        return nameMap.getOrDefault(nameCheck, defaultName);
    }

    public static ArrayList<String> getValidNames() {
        return validNames;
    }
}
