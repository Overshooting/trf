package com.gmail.aamelis.trf.ModNPCs.Quests.Objectives;

import com.gmail.aamelis.trf.ModAttachments.QuestAttachments.QuestProgress;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

public class KillObjective implements QuestObjective{

    private final EntityType<?> target;
    private final int required;

    public KillObjective(EntityType<?> target, int required) {
        this.target = target;
        this.required = required;
    }

    @Override
    public boolean isComplete(Player player, QuestProgress progress) {
        return progress.getKillCount(target) >= required;
    }

    @Override
    public void onKill(ServerPlayer player, QuestProgress progress, EntityType<?> type) {
        if (type == target) {
            progress.incrementKill(target);
        }
    }
}
