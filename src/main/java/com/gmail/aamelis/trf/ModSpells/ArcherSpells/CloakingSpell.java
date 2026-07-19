package com.gmail.aamelis.trf.ModSpells.ArcherSpells;

import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;
import com.gmail.aamelis.trf.ModPlayerData.ModStats.PlayerStatData;
import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.Network.Packets.SpellAnimationPacket;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
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
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CloakingSpell implements ISpell {
    @Override
    public String getId() {
        return "cloaking_tactics";
    }

    @Override
    public String getDisplayName() {
        return "Cloaking Tactics";
    }

    @Override
    public short getRequiredClass() {
        return PlayerSpellData.ARCHER;
    }

    @Override
    public int getRequiredMana() {
        return 100;
    }

    @Override
    public long getCooldown() {
        return 10000;
    }

    @Override
    public int multiCastTicks() {
        return 0;
    }

    @Override
    public int repetitions() {
        return 0;
    }

    @Override
    public void cast(ServerPlayer player) {
        ServerLevel level = player.level();

        double radius = 6.0;
        AABB box = player.getBoundingBox().inflate(radius);

        List<ServerPlayer> nearbyPlayers = level.getEntitiesOfClass(ServerPlayer.class, box, p ->
                p != player && p.distanceToSqr(player) <= radius * radius);

        int playersAffected = 1 + nearbyPlayers.size();

        PlayerStatData data = player.getData(AttachmentTypesInit.PLAYER_STATS);

        for (ServerPlayer serverPlayer : nearbyPlayers) {
            applyEffect(serverPlayer, level, data.getPerception());
        }

        applyEffect(player, level, data.getPerception());

        player.sendSystemMessage(Component.literal(playersAffected + " player" + (playersAffected == 1 ? "" : "s") + " shielded by Cloaking Tactics!").withStyle(ChatFormatting.GOLD));
    }

    private void applyEffect(ServerPlayer player, ServerLevel level, int perceptionLevel) {
        int finalDuration = (int)Math.ceil(60 + (0.1 * perceptionLevel));

        player.addEffect(new MobEffectInstance(MobEffects.SPEED, finalDuration, 20));
        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, finalDuration, 20));

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SCULK_SENSOR_BREAK, SoundSource.PLAYERS, 1.0f, 1.0f);

        level.sendParticles(ParticleTypes.SQUID_INK, player.getX(), player.getBlockY(), player.getZ(), 12, 0.8, 0.8, 0.8, 0.5);
    }

    @Override
    public void repeatedCast(ServerPlayer player, int iteration) {

    }

    @Override
    public List<SpellInput> getCombo() {
        return List.of(
                SpellInput.B,
                SpellInput.B,
                SpellInput.C
        );
    }

    @Override
    public ResourceLocation getFullPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/cloak_full.png");
    }

    @Override
    public ResourceLocation getEmptyPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/cloak_empty.png");
    }

    @Override
    public ResourceLocation animationId() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "animation.player.cast_cloak");
    }
}
