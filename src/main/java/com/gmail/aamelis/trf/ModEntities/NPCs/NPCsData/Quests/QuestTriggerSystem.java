package com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.Quests;

import com.gmail.aamelis.trf.ModAttachments.QuestAttachments.PlayerQuestData;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf.Registries.QuestsInit;
import net.minecraft.server.level.ServerPlayer;

public class QuestTriggerSystem {

    public static void fire(ServerPlayer player, String triggerId) {
        PlayerQuestData data = player.getData(AttachmentTypesInit.PLAYER_QUEST_DATA);

        for (var entry : data.getAll().entrySet()) {
            var questId = entry.getKey();
            var progress = entry.getValue();

            var questLine = QuestsInit.QUESTS.get(questId);
            if (questLine == null) continue;

            int stageIndex = progress.getStage();
            if (stageIndex >= questLine.stages().size()) continue;

            var stage = questLine.stages().get(stageIndex);

            for (var obj : stage.objectives()) {
                obj.onTrigger(player, progress, triggerId);
            }
        }
    }

}
