package com.gmail.aamelis.trf_gi.ModKeybinds;

import com.gmail.aamelis.trf_gi.TRFGearAndItemsFinalRegistry;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class CastKeybinds {

    public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(
            ResourceLocation.fromNamespaceAndPath(TRFGearAndItemsFinalRegistry.MODID, "spell_keys"));

    public static final KeyMapping SPELL_C =
            new KeyMapping("key.trf_gi.spell_c",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_C,
                    CATEGORY);

    public static final KeyMapping SPELL_V =
            new KeyMapping("key.trf_gi.spell_v",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_V,
                    CATEGORY);

    public static final KeyMapping SPELL_B =
            new KeyMapping("key.trf_gi.spell_b",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_B,
                    CATEGORY);

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(SPELL_C);
        event.register(SPELL_V);
        event.register(SPELL_B);
    }
}
