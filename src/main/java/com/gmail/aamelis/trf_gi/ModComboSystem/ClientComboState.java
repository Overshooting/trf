package com.gmail.aamelis.trf_gi.ModComboSystem;

import com.gmail.aamelis.trf_gi.ModSpells.SpellInput;

import java.util.ArrayList;
import java.util.List;

public class ClientComboState {

    public static ArrayList<SpellInput> currentInputs = new ArrayList<>();

    public static String resultMessage = "";

    public static long resultTime = 0, lastUpdateTime = 0;

}
