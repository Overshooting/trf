package com.gmail.aamelis.trf.ModNPCs.Dialog;

import com.gmail.aamelis.trf.ModEntities.Projectiles.PaintedPantheonProjectile;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.*;

public class DialogScheduler {

    private static final Map<UUID, List<ScheduledMessage>> QUEUE = new HashMap<>();

    public static void schedule(ServerPlayer player, List<String> lines) {
        UUID id = player.getUUID();

        if (QUEUE.containsKey(id)) return;

        List<ScheduledMessage> queue = new ArrayList<>();

        int delay = 0;
        for (String line : lines) {
            queue.add(new ScheduledMessage(line, delay));
            delay += 40;
        }

        QUEUE.put(id, queue);
    }

    public static void tick(ServerLevel level) {
        Iterator<Map.Entry<UUID, List<ScheduledMessage>>> mapIt = QUEUE.entrySet().iterator();

        while (mapIt.hasNext()) {
            var entry = mapIt.next();
            UUID id = entry.getKey();

            ServerPlayer player = level.getServer().getPlayerList().getPlayer(id);

            if (player == null) {
                mapIt.remove();
                continue;
            }

            List<ScheduledMessage> messages = entry.getValue();
            Iterator<ScheduledMessage> it = messages.iterator();

            while (it.hasNext()) {
                ScheduledMessage msg = it.next();
                msg.delay--;

                if (msg.delay <= 0) {
                    player.sendSystemMessage(Component.literal(msg.text));
                    it.remove();
                }
            }

            if (messages.isEmpty()) {
                mapIt.remove();
            }
        }
    }

}
