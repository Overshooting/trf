package com.gmail.aamelis.trf.ModComboSystem;

import com.gmail.aamelis.trf.ModSpells.SpellInput;

import java.util.ArrayList;

public class ClientComboState {

    public static ArrayList<SpellInput> currentInputs = new ArrayList<>();

    public static String resultMessage = "";

    public static long resultTime = 0, lastUpdateTime = 0;

}
