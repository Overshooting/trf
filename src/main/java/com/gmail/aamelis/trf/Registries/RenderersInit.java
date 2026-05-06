package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModEntities.NPCs.Rendering.NPCModel;
import com.gmail.aamelis.trf.ModEntities.NPCs.Rendering.NPCRenderer;
import com.gmail.aamelis.trf.ModEntities.Projectiles.Rendering.ParticleProjectileRenderer;
import com.gmail.aamelis.trf.ModScreens.GameMasterBlockScreen;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class RenderersInit {

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                EntitiesInit.STAFF_PROJECTILE.get(),
                ParticleProjectileRenderer::new
        );

        event.registerEntityRenderer(
                EntitiesInit.SUNLIGHT_REACH_PROJECTILE.get(),
                ParticleProjectileRenderer::new
        );

        event.registerEntityRenderer(
                EntitiesInit.MANA_BLAST_PROJECTILE.get(),
                ParticleProjectileRenderer::new
        );

        event.registerEntityRenderer(
                EntitiesInit.FLAVOR_NPC_ENTITY.get(),
                NPCRenderer::new
        );

        event.registerEntityRenderer(
                EntitiesInit.STEP_QUEST_NPC_ENTITY.get(),
                NPCRenderer::new
        );

        event.registerEntityRenderer(
                EntitiesInit.TUTORIAL_STEP_QUEST_NPC_ENTITY.get(),
                NPCRenderer::new
        );
    }

    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(MenuTypesInit.GAME_MASTER_BLOCK_MENU.get(), GameMasterBlockScreen::new);
    }

    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(NPCModel.LAYER_LOCATION, NPCModel::createBodyLayer);
    }

}
