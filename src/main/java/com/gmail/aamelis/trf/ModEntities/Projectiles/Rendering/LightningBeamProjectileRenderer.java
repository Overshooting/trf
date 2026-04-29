package com.gmail.aamelis.trf.ModEntities.Projectiles.Rendering;

import com.gmail.aamelis.trf.ModEntities.Projectiles.LightningBeamProjectile;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class LightningBeamProjectileRenderer extends EntityRenderer<LightningBeamProjectile, EntityRenderState> {
    public LightningBeamProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

}
