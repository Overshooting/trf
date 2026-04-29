package com.gmail.aamelis.trf.ModSpellRendering.LightningBeam;

import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ClientLightningBeamManager {

    private static final List<LightningBeamInstance> BEAMS = new ArrayList<>();

    public static void addBeam(Vec3 start, Vec3 end, Vec3 impact) {
        BEAMS.add(new LightningBeamInstance(start, end, impact, 5));
    }

    public static void tick() {
        BEAMS.removeIf(LightningBeamInstance::tick);
    }

    public static List<LightningBeamInstance> getBeams() {
        return BEAMS;
    }

}
