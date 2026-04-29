package com.gmail.aamelis.trf.ModSpellRendering.LightningBeam;

import net.minecraft.world.phys.Vec3;

public class LightningBeamInstance {

    public final Vec3 start;
    public final Vec3 end;
    public final Vec3 impact;
    private int life;

    public LightningBeamInstance(Vec3 start, Vec3 end, Vec3 impact, int life) {
        this.start = start;
        this.end = end;
        this.impact = impact;
        this.life = life;
    }

    public boolean tick() {
        return --life <= 0;
    }

}
