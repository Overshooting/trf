package com.gmail.aamelis.trf.ModPlayerData.ModStats.Keybinds;

import com.gmail.aamelis.trf.TRFFinalRegistry;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.checkerframework.checker.units.qual.C;
import org.checkerframework.checker.units.qual.K;
import org.lwjgl.glfw.GLFW;

public class StatKeybinds {

    public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(
            ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "stat_sheet_keys")
    );

    public static final KeyMapping TOGGLE_STAT_SHEET =
            new KeyMapping("key.trf.toggle_stat_sheet",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_N,
                    CATEGORY);

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_STAT_SHEET);
    }

}
