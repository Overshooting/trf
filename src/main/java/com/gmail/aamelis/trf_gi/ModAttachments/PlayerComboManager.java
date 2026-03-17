package com.gmail.aamelis.trf_gi.ModAttachments;

import com.gmail.aamelis.trf_gi.ModComboSystem.ComboBuffer;
import net.minecraft.server.level.ServerPlayer;
import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerComboManager {

    private static final Map<UUID, ComboBuffer> buffers = new HashMap<>();

    public static ComboBuffer getBuffer(ServerPlayer player) {
        return buffers.computeIfAbsent(player.getUUID(),
                uuid -> new ComboBuffer());
    }

}
