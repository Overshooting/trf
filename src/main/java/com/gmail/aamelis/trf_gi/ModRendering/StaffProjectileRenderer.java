package com.gmail.aamelis.trf_gi.ModRendering;

import com.gmail.aamelis.trf_gi.ModEntities.StaffProjectile;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class StaffProjectileRenderer extends EntityRenderer<StaffProjectile, EntityRenderState> {

    public StaffProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}
