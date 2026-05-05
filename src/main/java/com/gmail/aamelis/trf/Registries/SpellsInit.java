package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModCastingSystem.Combo.ComboUtils;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.ModSpells.MageSpells.*;
import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SpellsInit {

    private static final ISpell[][] SPELLS = new ISpell[4][27];

    public static void register(ISpell spell) {
        List<SpellInput> combo = spell.getCombo();

        int index = ComboUtils.toIndex(combo.get(0), combo.get(1), combo.get(2));
        int classNumber = spell.getRequiredClass() - 1;

        if (SPELLS[classNumber][index] != null) {
            throw new IllegalStateException("Duplicate combo for class + " + (classNumber + 1) + " at index: " + index);
        }

        SPELLS[classNumber][index] = spell;
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

    public static void register() {
        register(new DispelSpell());
        register(new ShadowSpell());
        register(new LightningBeamSpell());
        register(new ManaBlastSpell());
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
