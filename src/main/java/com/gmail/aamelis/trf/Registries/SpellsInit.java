package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModCastingSystem.Combo.ComboUtils;
import com.gmail.aamelis.trf.ModSpells.ArcherSpells.*;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.ModSpells.MageSpells.*;
import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;
import com.gmail.aamelis.trf.ModSpells.WarriorSpells.DoubleCutSpell;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SpellsInit {

    private static final ISpell[][] SPELLS = new ISpell[4][27];
    private static final List<ISpell> register = new ArrayList<>();

    static {
        register.add(new DispelSpell());
        register.add(new ShadowSpell());
        register.add(new LightningBeamSpell());
        register.add(new ManaBlastSpell());
        register.add(new SunlightReachSpell());
        register.add(new PaintedPantheonSpell());
        register.add(new HyacinthBladeSpell());
        register.add(new ImbuePoisonSpell());
        register.add(new ImbueFrostSpell());
        register.add(new ImbueFireSpell());
        register.add(new PiercingShotSpell());
        register.add(new QuickShotSpell());
        register.add(new MonsterTrapSpell());
        register.add(new CloakingSpell());
        register.add(new DummyShotSpell());
        register.add(new DoubleCutSpell());
    }

    public static void registerSpells() {
        int count = 0;

        for (ISpell spell : register) {
            List<SpellInput> combo = spell.getCombo();

            int index = ComboUtils.toIndex(combo.get(0), combo.get(1), combo.get(2));
            int classNumber = spell.getRequiredClass() - 1;

            if (SPELLS[classNumber][index] != null) {
                throw new IllegalStateException("Duplicate combo for class + " + (classNumber + 1) + " at index: " + index);
            }

            System.out.println("Added spell: " + spell.getDisplayName() +
                    "\nwith combo: " + combo.get(0).toString() + ", " + combo.get(1).toString() + ", " +  combo.get(2).toString());

            SPELLS[classNumber][index] = spell;
            count++;
        }

        System.out.println("Registered " + count + " spells");
    }

    @Nullable
    public static ISpell get(short classNumber, SpellInput a, SpellInput b, SpellInput c) throws IllegalArgumentException {
        if (classNumber < 1 || classNumber > 4) return null;
        return SPELLS[classNumber - 1][ComboUtils.toIndex(a, b, c)];
    }

    @Nullable
    public static ISpell get(String id) {
        for (ISpell[] iSpells : SPELLS) {
            for (ISpell spell : iSpells) {
                if (spell != null && spell.getId().equals(id)) {
                    return spell;
                }
            }
        }

        return null;
    }

    public static List<String> getAllSpellsForClass(short num) {
        if (num < 1 || num > 4) return List.of();

        List<String> returned = new ArrayList<>();

        for (ISpell spell : SPELLS[num - 1]) {
            if (spell != null) {
                System.out.println("Found spell: " + spell.getId());

                returned.add(spell.getDisplayName());
            }
        }

        return returned;
    }

    public static List<String> getAllSpellNames() {
        List<String> returned = new ArrayList<>();

        for (ISpell[] iSpells : SPELLS) {
            for (ISpell spell : iSpells) {
                if (spell != null) {
                    returned.add(spell.getDisplayName());
                }
            }
        }

        return returned;
    }

}
