package com.gmail.aamelis.trf.ModCastingSystem.Keybinds;

public enum SpellInput {
    C(0),
    V(1),
    B(2);

    private final int index;

    SpellInput(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
