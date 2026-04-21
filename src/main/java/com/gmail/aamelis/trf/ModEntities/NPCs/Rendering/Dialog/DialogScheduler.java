package com.gmail.aamelis.trf.ModEntities.NPCs.Rendering.Dialog;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

public class DialogScheduler {

    private static final Map<ServerPlayer, List<ScheduledMessage>> QUEUE = new HashMap<>();

    public static void schedule(ServerPlayer player, String text, int delay) {
        QUEUE.computeIfAbsent(player, p -> new ArrayList<>())
                .add(new ScheduledMessage(text, delay));
    }

    public static void tick() {
        for (var entry : QUEUE.entrySet()) {
            ServerPlayer player = entry.getKey();
            Iterator<ScheduledMessage> it = entry.getValue().iterator();

            while (it.hasNext()) {
                ScheduledMessage msg = it.next();
                msg.delay--;

                if (msg.delay <= 0) {
                    player.sendSystemMessage(Component.literal(msg.text));
                    it.remove();
                }
            }
        }
    }

}
