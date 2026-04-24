package com.gmail.aamelis.trf.ModNPCs.NPCsData;

import java.util.ArrayList;
import java.util.HashMap;


public enum NPCArea {
    DEFAULT("null"),
    STARTER_TOWN("starter_town"),
    COLONIAL_TOWN("colonial_town");

    private final String readableName;
    private static final HashMap<String, NPCArea> areaMap = new HashMap<>();
    private static final ArrayList<String> validReadableNames = new ArrayList<>();

    static {
        for (NPCArea thisArea : NPCArea.values()) {
            areaMap.put(thisArea.readableName, thisArea);
            if (thisArea != DEFAULT) {
                validReadableNames.add(thisArea.readableName);
            }
        }
    }

    NPCArea(String name) {
        readableName = name;
    }

    public String getReadableName() {
        return readableName;
    }

    public static NPCArea matchReadableNameOrDefault(String nameCheck, NPCArea defaultName) {
        return areaMap.getOrDefault(nameCheck, defaultName);
    }

    public static ArrayList<String> getValidReadableNames() {
        return validReadableNames;
    }
}
