package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModComboSystem.ComboUtils;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.ModSpells.MageSpells.DispelSpell;
import com.gmail.aamelis.trf.ModSpells.SpellInput;

import java.util.List;

public class SpellsInit {

    private static final ISpell[] SPELLS = new ISpell[27];

    public static void register(ISpell spell) {
        List<SpellInput> combo = spell.getCombo();

        int index = ComboUtils.toIndex(combo.get(0), combo.get(1), combo.get(2));

        if (SPELLS[index] != null) {
            throw new IllegalStateException("Duplicate combo at index: " + index);
        }

        SPELLS[index] = spell;
    }

    public static ISpell get(SpellInput a, SpellInput b, SpellInput c) {
        return SPELLS[ComboUtils.toIndex(a, b, c)];
    }

    public static void register() {
        register(new DispelSpell());
    }

}
