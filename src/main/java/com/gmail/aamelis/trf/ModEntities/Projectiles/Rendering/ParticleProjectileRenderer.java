package com.gmail.aamelis.trf.ModEntities.Projectiles.Rendering;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.projectile.ThrowableProjectile;

public class ParticleProjectileRenderer extends EntityRenderer<ThrowableProjectile, EntityRenderState> {

    public ParticleProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}
