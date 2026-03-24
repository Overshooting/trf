package com.gmail.aamelis.trf.ModComboSystem;

import com.gmail.aamelis.trf.ModSpells.SpellInput;

public class ComboBuffer {

    private final SpellInput[] inputs = new SpellInput[3];
    private int size = 0;

    private long lastInputTime = 0;

    public void addInput(SpellInput input) {
        long now = System.currentTimeMillis();

        if (now - lastInputTime > 1000) {
            clear();
        }

        lastInputTime = now;

        if (size < 3) {
            inputs[size++] = input;
        }
    }

    public boolean isFull() {
        return size == 3;
    }

    public SpellInput[] getInputs() {
        return inputs;
    }

    public void clear() {
        size = 0;
    }

    public int getSize() {
        return size;
    }

    public long getLastInputTime() {
        return lastInputTime;
    }

}
