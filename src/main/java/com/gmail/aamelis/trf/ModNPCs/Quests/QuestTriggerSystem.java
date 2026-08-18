package com.gmail.aamelis.trf.ModNPCs.Quests;

import com.gmail.aamelis.trf.ModGlobalData.GlobalQuestData;
import com.gmail.aamelis.trf.ModPlayerData.QuestPlayerData.PlayerQuestData;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf.Registries.QuestsInit;
import net.minecraft.server.level.ServerPlayer;

public class QuestTriggerSystem {

    public static void fire(ServerPlayer player, String triggerId) {
        QuestsInit.forEachActiveObjective(player, (obj, progress) ->
                obj.onTrigger(player, progress, triggerId));
    }

    public static void fireGlobal(ServerPlayer player, String triggerId) {
        QuestsInit.forEachGlobalActiveObjective(player.level(), (obj, progress) ->
                obj.onTrigger(player, progress, triggerId));
    }
}
