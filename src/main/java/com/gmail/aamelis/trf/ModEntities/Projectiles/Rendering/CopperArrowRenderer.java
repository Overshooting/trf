package com.gmail.aamelis.trf.ModEntities.Projectiles.Rendering;

import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class CopperArrowRenderer extends AbstractArrowRenderer {
    public CopperArrowRenderer(EntityRendererProvider.Context context) {
        super(context, ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/entity/projectiles/copper_arrow.png"));
    }
}
