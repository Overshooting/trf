package com.gmail.aamelis.trf.ModSpells.MageSpells;

import com.gmail.aamelis.trf.ModAttachments.PlayerSpellData;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;
import com.gmail.aamelis.trf.Network.Packets.SpellAnimationPacket;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DispelSpell implements ISpell {
    @Override
    public String getId() {
        return "dispel";
    }

    @Override
    public String getDisplayName() {
        return "Dispel";
    }

    @Override
    public short getRequiredClass() {
        return PlayerSpellData.MAGE;
    }

    @Override
    public int getRequiredMana() {
        return 50;
    }

    @Override
    public long getCooldown() {
        return 1000;
    }

    @Override
    public void cast(ServerPlayer player) {
        Collection<MobEffectInstance> activeEffects = player.getActiveEffects();
        ArrayList<MobEffectInstance> effectsToRemove = new ArrayList<>();
        int effectsRemoved = 0;

        for (MobEffectInstance instance : activeEffects) {
            if (instance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
                effectsToRemove.add(instance);
            }
        }

        for (MobEffectInstance instance : effectsToRemove) {
            player.removeEffect(instance.getEffect());
            effectsRemoved++;
        }

        ServerLevel level = player.level();

        ResourceLocation animId = animationId();

        SpellAnimationPacket packet = new SpellAnimationPacket(player.getUUID(), animId.toString());

        PacketDistributor.sendToPlayer(player, packet);
        PacketDistributor.sendToPlayersNear(level, player, player.getX(), player.getY(), player.getZ(), 64.0, packet);

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0f, 1.0f);

        level.sendParticles(ParticleTypes.GLOW_SQUID_INK, player.getX(), player.getBlockY(), player.getZ(), 50, 0.8, 0.8, 0.8, 0.5);

        player.sendSystemMessage(Component.literal(effectsRemoved + " harmful effects removed by Dispel!").withStyle(
                effectsRemoved > 0 ? ChatFormatting.GOLD : ChatFormatting.DARK_GRAY));
    }

    @Override
    public List<SpellInput> getCombo() {
        return List.of(
                SpellInput.V,
                SpellInput.C,
                SpellInput.C
        );
    }

    @Override
    public ResourceLocation getEmptyPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/dispel_empty.png");
    }

    @Override
    public ResourceLocation getFullPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/dispel_full.png");
    }

    @Override
    public ResourceLocation animationId() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "animation.player.cast_dispel");
    }
}
