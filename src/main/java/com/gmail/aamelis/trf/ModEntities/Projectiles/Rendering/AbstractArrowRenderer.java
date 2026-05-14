package com.gmail.aamelis.trf.ModEntities.Projectiles.Rendering;

import com.gmail.aamelis.trf.ModEntities.Projectiles.ArrowProjectiles.AbstractImbueableArrow;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.ResourceLocation;

public class AbstractArrowRenderer extends ArrowRenderer<AbstractImbueableArrow, ArrowRenderState> {

    private ResourceLocation texture;

    public AbstractArrowRenderer(EntityRendererProvider.Context context, ResourceLocation texture) {
        super(context);

        this.texture = texture;
    }

    @Override
    public ArrowRenderState createRenderState() {
        return new ArrowRenderState();
    }

    @Override
    protected ResourceLocation getTextureLocation(ArrowRenderState state) {
        return texture;
    }


}
