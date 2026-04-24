package com.gmail.aamelis.trf.ModCastingSystem.Combo;

import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;

import java.util.ArrayList;

public class ClientComboState {

    public static final int NOT_FINISHED = 0;
    public static final int SUCCESS = 1;
    public static final int FAILURE = 2;


    public static ArrayList<SpellInput> currentInputs = new ArrayList<>();

    public static int resultState = NOT_FINISHED;

    public static long resultTime = 0, lastUpdateTime = 0;

}
