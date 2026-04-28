package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModNPCs.DataLoaders.QuestDataLoader;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

public class DataLoadersInit {

    public static void register(AddServerReloadListenersEvent event) {
        event.addListener(ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "quest_loader"), new QuestDataLoader());
    }

}
