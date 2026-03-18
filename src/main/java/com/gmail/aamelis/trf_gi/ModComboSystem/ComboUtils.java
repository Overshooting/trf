package com.gmail.aamelis.trf_gi.ModComboSystem;

import com.gmail.aamelis.trf_gi.ModSpells.SpellInput;

public class ComboUtils {

    public static int toIndex(SpellInput a, SpellInput b, SpellInput c) {
        return a.getIndex() * 9 + b.getIndex() * 3 + c.getIndex();
    }

}
