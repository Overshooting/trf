package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModEntities.NPCs.Types.FlavorNPCEntity;
import com.gmail.aamelis.trf.ModEntities.NPCs.Types.StepQuestNPCEntity;
import com.gmail.aamelis.trf.ModEntities.NPCs.Types.TutorialStepQuestNPCEntity;
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

    public static final DeferredHolder<EntityType<?>, EntityType<FlavorNPCEntity>> FLAVOR_NPC_ENTITY =
            ENTITIES.register("flavor_npc_entity", () ->
                    EntityType.Builder.of(
                            FlavorNPCEntity::new,
                            MobCategory.MISC
                    )
                            .sized(1.0f, 1.0f)
                            .build(ResourceKey.create(
                            Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "flavor_npc_entity")
                    )));

    public static final DeferredHolder<EntityType<?>, EntityType<StepQuestNPCEntity>> STEP_QUEST_NPC_ENTITY =
            ENTITIES.register("step_quest_npc_entity", () ->
                    EntityType.Builder.of(
                                    StepQuestNPCEntity::new,
                                    MobCategory.MISC
                            )
                            .sized(1.0f, 1.0f)
                            .build(ResourceKey.create(
                                    Registries.ENTITY_TYPE,
                                    ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "step_quest_npc_entity")
                            )));

    public static final DeferredHolder<EntityType<?>, EntityType<TutorialStepQuestNPCEntity>> TUTORIAL_STEP_QUEST_NPC_ENTITY =
            ENTITIES.register("tutorial_step_quest_npc_entity", () ->
                    EntityType.Builder.of(
                                    TutorialStepQuestNPCEntity::new,
                                    MobCategory.MISC
                            )
                            .sized(1.0f, 1.0f)
                            .build(ResourceKey.create(
                                    Registries.ENTITY_TYPE,
                                    ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "tutorial_step_quest_npc_entity")
                            )));

    public static void register(IEventBus modEventBus) {
        ENTITIES.register(modEventBus);
    }
}
