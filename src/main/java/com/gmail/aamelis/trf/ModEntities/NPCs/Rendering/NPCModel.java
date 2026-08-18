package com.gmail.aamelis.trf.ModEntities.NPCs.Rendering;

import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class NPCModel extends EntityModel<NPCRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "npc_model"), "main");

    public NPCModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(NPCRenderState state) {

    }
}
