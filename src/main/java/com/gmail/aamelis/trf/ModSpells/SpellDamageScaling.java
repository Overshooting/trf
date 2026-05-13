package com.gmail.aamelis.trf.ModSpells;

public class SpellDamageScaling {

    public static float scaleDamage(float base, int magicStat) {
        double sqrt = Math.sqrt(magicStat);
        double log = Math.log(magicStat + 1);

        double multiplier = 1.0
                + (0.12 * sqrt)
                + (0.05 * log);

        return (float) (base * multiplier);
    }

}
