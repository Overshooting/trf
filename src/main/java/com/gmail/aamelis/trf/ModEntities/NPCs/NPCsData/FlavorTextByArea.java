package com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FlavorTextByArea {

    private static final HashMap<NPCArea, ArrayList<String>> TEXTS = new HashMap<>(Map.of(
            NPCArea.STARTER_TOWN, new ArrayList<>(Arrays.asList("Nice weather we're having today!", "The early bird gets the worm!")),
            NPCArea.COLONIAL_TOWN, new ArrayList<>(Arrays.asList("I was just heading down to the market.", "Jack is the best blacksmith in town!"))
    ));

    public static String getRandomText(NPCArea area) {
        ArrayList<String> texts = TEXTS.get(area);

        if (texts == null) {
            return "They are watching...";
        }

        int  index = (int) (Math.random() * texts.size());
        return texts.get(index);
    }

}
