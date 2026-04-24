package com.gmail.aamelis.trf.ModNPCs.DataLoaders;

import com.mojang.serialization.Codec;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlavorTextLoader extends SimpleJsonResourceReloadListener<List<String>> {

    public static final Map<ResourceLocation, List<String>> TEXTS = new HashMap<>();

    public FlavorTextLoader() {
        super(Codec.STRING.listOf(), FileToIdConverter.json("flavor"));
    }

    @Override
    protected void apply(Map<ResourceLocation, List<String>> resourceLocationListMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        TEXTS.clear();
        TEXTS.putAll(resourceLocationListMap);
    }
}
