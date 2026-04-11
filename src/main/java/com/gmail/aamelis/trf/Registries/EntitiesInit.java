package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModEntities.NPCs.StarterTown.StarterTownFlavorEntity;
import com.gmail.aamelis.trf.ModEntities.Projectiles.StaffProjectile;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntitiesInit {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, TRFFinalRegistry.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<StaffProjectile>> STAFF_PROJECTILE =
            ENTITIES.register("staff_projectile", () ->
                    EntityType.Builder.<StaffProjectile>of(
                            StaffProjectile::new,
                            MobCategory.MISC
                    )
                            .sized(0.75f, 0.75f)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build(ResourceKey.create(
                                    Registries.ENTITY_TYPE,
                                    ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "staff_projectile")
                            )));

    public static final DeferredHolder<EntityType<?>, EntityType<StarterTownFlavorEntity>> STARTER_TOWN_FLAVOR_ENTITY =
            ENTITIES.register("starter_town_flavor_entity", () ->
                    EntityType.Builder.of(
                            StarterTownFlavorEntity::new,
                            MobCategory.MISC
                    )
                            .sized(1.0f, 1.0f)
                            .build(ResourceKey.create(
                            Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "starter_town_flavor")
                    )));

    public static void register(IEventBus modEventBus) {
        ENTITIES.register(modEventBus);
    }
}
