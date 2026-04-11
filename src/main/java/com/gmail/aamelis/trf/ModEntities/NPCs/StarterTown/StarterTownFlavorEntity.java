package com.gmail.aamelis.trf.ModEntities.NPCs.StarterTown;

import com.gmail.aamelis.trf.ModEntities.NPCs.AbstractFlavorEntity;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.NPCArea;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.NPCName;
import com.gmail.aamelis.trf.Registries.EntitiesInit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class StarterTownFlavorEntity extends AbstractFlavorEntity {
    public StarterTownFlavorEntity(EntityType<? extends AbstractFlavorEntity> entityType, Level p_20967_) {
        super(entityType, NPCArea.STARTER_TOWN, NPCName.WILLIAM, p_20967_);
    }
}
