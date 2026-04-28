package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModEntities.NPCs.Types.AbstractNPCEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

public class AttributesInit {

    public static void register(EntityAttributeCreationEvent event) {
        event.put(EntitiesInit.FLAVOR_NPC_ENTITY.get(), AbstractNPCEntity.createMobAttributes().build());
        event.put(EntitiesInit.STEP_QUEST_NPC_ENTITY.get(), AbstractNPCEntity.createMobAttributes().build());
        event.put(EntitiesInit.TUTORIAL_STEP_QUEST_NPC_ENTITY.get(), AbstractNPCEntity.createMobAttributes().build());
    }

}
