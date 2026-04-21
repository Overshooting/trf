package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.NPCName;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.Quests.QuestLine;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.Quests.TutorialQuestLines;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestsInit {

    public static final Map<ResourceLocation, QuestLine> QUESTS = new HashMap<>();
    public static final Map<NPCName, ResourceLocation> NPC_TO_QUEST = new HashMap<>();

    public static final ResourceLocation HEAD_GENERAL = ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "head_general_questline");

    static {
        QuestLine questline = new QuestLine(HEAD_GENERAL, List.of(TutorialQuestLines.HEAD_GENERAL_QUESTLINE));

        QUESTS.put(HEAD_GENERAL, questline);
        NPC_TO_QUEST.put(NPCName.HEAD_GENERAL, HEAD_GENERAL);
    }

}
