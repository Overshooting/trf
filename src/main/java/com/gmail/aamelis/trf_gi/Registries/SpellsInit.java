package com.gmail.aamelis.trf_gi.Registries;

import com.gmail.aamelis.trf_gi.ModSpells.FireSpell;
import com.gmail.aamelis.trf_gi.ModSpells.ISpell;
import com.gmail.aamelis.trf_gi.ModSpells.IceSpell;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SpellsInit {

    private static final Map<String, ISpell> SPELLS = new HashMap<>();

    public static void register(ISpell spell) {
        SPELLS.put(spell.getId(), spell);
    }

    public static ISpell getById(String id) {
        return SPELLS.get(id);
    }

    public static Collection<ISpell> getAllSpells() {
        return SPELLS.values();
    }

    public static boolean exists(String id) {
        return SPELLS.containsKey(id);
    }

    public static void register() {
        register(new FireSpell());
        register(new IceSpell());
    }

}
