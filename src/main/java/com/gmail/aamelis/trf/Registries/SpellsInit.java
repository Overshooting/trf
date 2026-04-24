package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModCastingSystem.Combo.ComboUtils;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.ModSpells.MageSpells.*;
import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;

import javax.annotation.Nullable;
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

    @Nullable
    public static ISpell get(String id) {
        for (ISpell i : SPELLS) {
            if (i != null && i.getId().equals(id)) {
                return i;
            }
        }

        return null;
    }

    public static void register() {
        register(new DispelSpell());
        register(new ShadowSpell());
    }

}
