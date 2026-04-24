package com.gmail.aamelis.trf.ModSpells.MageSpells;

import com.gmail.aamelis.trf.ModAttachments.PlayerSpellData;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ShadowSpell implements ISpell {
    @Override
    public String getId() {
        return "shadow_step";
    }

    @Override
    public String getDisplayName() {
        return "Shadow Step";
    }

    @Override
    public short getRequiredClass() {
        return PlayerSpellData.MAGE;
    }

    @Override
    public int getRequiredMana() {
        return 30;
    }

    @Override
    public long getCooldown() {
        return 7000;
    }

    @Override
    public void cast(ServerPlayer player) {
        player.level().playSound(null, player.blockPosition(), SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.PLAYERS, 60.0f, 0.8f);
        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 15, 255));

        Vec3 start = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        Vec3 end = start.add(look.scale(10));

        ServerLevel level = player.level();

        var result = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        Vec3 target;

        if (result.getType() == HitResult.Type.BLOCK) {
            target = result.getLocation().subtract(look.scale(0.5));
        } else {
            target = end;
        }

        player.teleportTo(target.x, target.y, target.z);

        player.sendSystemMessage(Component.literal("Shadow cast successfully!"));
    }

    @Override
    public List<SpellInput> getCombo() {
        return List.of(
                SpellInput.V,
                SpellInput.V,
                SpellInput.V
        );
    }

    @Override
    public ResourceLocation getEmptyPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/shadow_step_empty.png");
    }

    @Override
    public ResourceLocation getFullPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/shadow_step_full.png");
    }

    @Override
    public ResourceLocation animationId() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "animation.player.cast_shadow_step");
    }
}
