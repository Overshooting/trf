package com.gmail.aamelis.trf.ModAttachments;

import com.gmail.aamelis.trf.ModCastingSystem.Combo.ComboBuffer;
import net.minecraft.server.level.ServerPlayer;

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
