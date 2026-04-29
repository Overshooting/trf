package com.gmail.aamelis.trf.ModSpellRendering.LightningBeam;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LightningBeamRenderer {

    private static final ResourceLocation BEAM_TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/entity/beacon_beam.png");

    public static void render(PoseStack stack, float partialtick) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        Camera cam = Minecraft.getInstance().gameRenderer.getMainCamera();
        double camX = cam.getPosition().x(), camY = cam.getPosition().y(), camZ = cam.getPosition().z();

        for (LightningBeamInstance beam : ClientLightningBeamManager.getBeams()) {
            Vec3 start = beam.start;
            Vec3 end = beam.end;

            Vec3 dir = end.subtract(start);
            double length = dir.length();

            if (length <= 0.1) continue;

            dir = dir.normalize();

            stack.pushPose();
            stack.translate(
                    start.x - camX,
                    start.y - camY,
                    start.z - camZ
            );

            float yaw = (float) Math.atan2(dir.x, dir.y);
            float pitch = (float) Math.asin(-dir.y);

            stack.mulPose(Axis.YP.rotation(yaw));
            stack.mulPose(Axis.XP.rotation(pitch));

            //BeaconRenderer.submitBeaconBeam(stack, source, BEAM_TEXTURE, partialtick, 1.0f, level.getGameTime(), 0, (int) length, new float[]{3.0f, 0.7f, 1.0f}, 0.2f, 0.25f);

            stack.popPose();

            for (int i = 0; i < 6; i++) {
                level.addParticle(
                        ParticleTypes.ELECTRIC_SPARK,
                        beam.impact.x,
                        beam.impact.y,
                        beam.impact.z,
                        (level.random.nextDouble() - 0.5) * 0.3,
                        (level.random.nextDouble() - 0.5) * 0.3,
                        (level.random.nextDouble() - 0.5) * 0.3
                );
            }
        }
    }

}
