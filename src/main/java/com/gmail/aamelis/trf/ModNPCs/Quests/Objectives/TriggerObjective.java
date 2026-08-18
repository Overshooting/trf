package com.gmail.aamelis.trf.ModNPCs.Quests.Objectives;

import com.gmail.aamelis.trf.ModPlayerData.QuestPlayerData.QuestProgress;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class TriggerObjective implements QuestObjective{

    private final String triggerId;
    private final int required;

    public TriggerObjective(String triggerId, int required) {
        this.triggerId = triggerId;
        this.required = required;
    }

    @Override
    public boolean isComplete(ServerPlayer player, QuestProgress progress) {
        return progress.getTriggerCount(triggerId) >= required;
    }

    @Override
    public void onTrigger(ServerPlayer player, QuestProgress progress, String triggerId) {
        if (triggerId.equals(this.triggerId)) {
            progress.incrementTrigger(triggerId);
        }
    }

    public String getTriggerId() {
        return triggerId;
    }

    public int getRequired() {
        return required;
    }

    @Override
    public String type() {
        return "trigger";
    }
}
