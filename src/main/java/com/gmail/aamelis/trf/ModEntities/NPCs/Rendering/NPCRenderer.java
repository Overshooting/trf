package com.gmail.aamelis.trf.ModEntities.NPCs.Rendering;

import com.gmail.aamelis.trf.ModEntities.NPCs.AbstractFlavorEntity;
import com.gmail.aamelis.trf.ModEntities.NPCs.StarterTown.StarterTownFlavorEntity;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.resources.ResourceLocation;
import org.checkerframework.checker.units.qual.N;

public class NPCRenderer extends MobRenderer<StarterTownFlavorEntity, NPCRenderState, NPCModel> {

    private static final ResourceLocation TEST_LOCATION = ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/entities/npcs/test_npc.png");

    public NPCRenderer(EntityRendererProvider.Context p_174304_, NPCModel p_174305_, float p_174306_) {
        super(p_174304_, p_174305_, p_174306_);
    }

    public NPCRenderer(EntityRendererProvider.Context p_174304_) {
        this(p_174304_, new NPCModel(p_174304_.bakeLayer(NPCModel.LAYER_LOCATION)), 0.25f);
    }

    @Override
    public ResourceLocation getTextureLocation(NPCRenderState state) {
        return TEST_LOCATION;
    }

    @Override
    public NPCRenderState createRenderState() {
        return new NPCRenderState();
    }
}
