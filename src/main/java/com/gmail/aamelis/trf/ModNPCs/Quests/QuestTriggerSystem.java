package com.gmail.aamelis.trf.ModNPCs.Quests;

import com.gmail.aamelis.trf.ModPlayerData.QuestPlayerData.PlayerQuestData;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf.Registries.QuestsInit;
import net.minecraft.server.level.ServerPlayer;

public class QuestTriggerSystem {

    public static void fire(ServerPlayer player, String triggerId) {
        PlayerQuestData data = player.getData(AttachmentTypesInit.PLAYER_QUEST_DATA);

        QuestsInit.forEachActiveObjective(player, (obj, progress) ->
                obj.onTrigger(player, progress, triggerId));
    }
}
