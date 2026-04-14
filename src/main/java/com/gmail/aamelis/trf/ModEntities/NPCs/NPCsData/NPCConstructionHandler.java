package com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData;

import com.gmail.aamelis.trf.ModEntities.NPCs.AbstractNPCEntity;
import com.gmail.aamelis.trf.ModEntities.NPCs.FlavorNPCEntity;
import com.gmail.aamelis.trf.Registries.EntitiesInit;
import net.minecraft.world.level.Level;

public class NPCConstructionHandler {

    public static FlavorNPCEntity buildFlavorNPC(NPCArea area, NPCName name, Level level) {
        FlavorNPCEntity thisEntity = new FlavorNPCEntity(EntitiesInit.FLAVOR_NPC_ENTITY.get(), level);
        thisEntity.setLocation(area);
        thisEntity.setName(name);

        return thisEntity;
    }

}
