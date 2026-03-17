package com.gmail.aamelis.trf_gi.Registries;

import com.gmail.aamelis.trf_gi.ModEntities.StaffProjectile;
import com.gmail.aamelis.trf_gi.TRFGearAndItemsFinalRegistry;
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
            DeferredRegister.create(Registries.ENTITY_TYPE, TRFGearAndItemsFinalRegistry.MODID);

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
                                    ResourceLocation.fromNamespaceAndPath(TRFGearAndItemsFinalRegistry.MODID, "staff_projectile")
                            )));

    public static void register(IEventBus modEventBus) {
        ENTITIES.register(modEventBus);
    }
}
