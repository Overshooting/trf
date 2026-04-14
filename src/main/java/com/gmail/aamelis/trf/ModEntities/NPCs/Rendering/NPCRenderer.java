package com.gmail.aamelis.trf.ModEntities.NPCs.Rendering;

import com.gmail.aamelis.trf.ModEntities.NPCs.AbstractNPCEntity;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class NPCRenderer extends MobRenderer<AbstractNPCEntity, NPCRenderState, NPCModel> {

    public NPCRenderer(EntityRendererProvider.Context p_174304_, NPCModel p_174305_, float p_174306_) {
        super(p_174304_, p_174305_, p_174306_);
    }

    public NPCRenderer(EntityRendererProvider.Context p_174304_) {
        this(p_174304_, new NPCModel(p_174304_.bakeLayer(NPCModel.LAYER_LOCATION)), 0.25f);
    }

    @Override
    public ResourceLocation getTextureLocation(NPCRenderState state) {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/entities/npcs/" + state.textureName + ".png");
    }

    @Override
    public NPCRenderState createRenderState() {
        return new NPCRenderState();
    }

    @Override
    public void extractRenderState(AbstractNPCEntity p_362733_, NPCRenderState p_360515_, float p_361157_) {
        super.extractRenderState(p_362733_, p_360515_, p_361157_);

        if (p_362733_.getNPCName() != null) {
            p_360515_.textureName = p_362733_.getEntityData().get(AbstractNPCEntity.DATA_TEXTURE);
        }
    }
}
