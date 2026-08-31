package com.gmail.aamelis.trf.ModUIRendering;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class RenderUtils {

    public static void renderSlashParticles(ServerPlayer player, ServerLevel level, boolean sideSlash) {
        Vec3 eyePos = player.getEyePosition();
        Vec3 lookAngle = player.getLookAngle();

        double slashX = eyePos.x + lookAngle.x;
        double slashY = player.getY() + player.getEyeHeight() - 0.5;
        double slashZ = eyePos.z + lookAngle.z;

        double xOffset = 0.0;
        double zOffset = 0.0;

        if (lookAngle.z > lookAngle.x) {
            zOffset = ((int)(Math.random() * 2) == 0 ? -1 : 1) *(Math.random() * 3) + 1;
        } else {
            xOffset = ((int)(Math.random() * 2) == 0 ? -1 : 1) *(Math.random() * 3) + 1;
        }

        if (sideSlash) {
            level.sendParticles(
                    ParticleTypes.CRIMSON_SPORE,
                    slashX,
                    slashY,
                    slashZ,
                    60,
                    xOffset,
                    0.0,
                    zOffset,
                    0.0
            );
        } else {
            level.sendParticles(
                    ParticleTypes.CRIMSON_SPORE,
                    slashX,
                    slashY,
                    slashZ,
                    40,
                    0.0,
                    (Math.random() * 2) + 1,
                    0.0,
                    0.0
            );
        }
    }

}
