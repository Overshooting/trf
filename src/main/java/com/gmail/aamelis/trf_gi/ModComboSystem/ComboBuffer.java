package com.gmail.aamelis.trf_gi.ModComboSystem;

import com.gmail.aamelis.trf_gi.ModSpells.SpellInput;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class ComboBuffer {

    private final Deque<SpellInput> inputs = new ArrayDeque<>(3);

    public void addInput(SpellInput input) {

        if (inputs.size() == 3) {
            inputs.removeFirst();
        }

        inputs.addLast(input);

    }

    public boolean isFull() {
        return inputs.size() == 3;
    }

    public List<SpellInput> getCombo() {
        return List.copyOf(inputs);
    }

    public void clear() {
        inputs.clear();
    }

}
