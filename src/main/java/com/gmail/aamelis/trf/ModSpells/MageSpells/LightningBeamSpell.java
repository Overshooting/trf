package com.gmail.aamelis.trf.ModSpells.MageSpells;

import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;
import com.gmail.aamelis.trf.ModCastingSystem.MultiStepSpells.MultiCastManager;
import com.gmail.aamelis.trf.ModCastingSystem.SpellAnimations;
import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.Network.Packets.SpellAnimationPacket;
import com.gmail.aamelis.trf.Registries.EffectsInit;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LightningBeamSpell implements ISpell {

    @Override
    public String getId() {
        return "lightning_beam";
    }

    @Override
    public String getDisplayName() {
        return "Lightning Beam";
    }

    @Override
    public short getRequiredClass() {
        return PlayerSpellData.MAGE;
    }

    @Override
    public int getRequiredMana() {
        return 100;
    }

    @Override
    public long getCooldown() {
        return 2000;
    }

    @Override
    public int multiCastTicks() {
        return 8;
    }

    @Override
    public int repetitions() {
        return 3;
    }

    @Override
    public void cast(ServerPlayer player) {
        ServerLevel level = player.level();

        ResourceLocation animId = animationId();

        SpellAnimationPacket packet = new SpellAnimationPacket(player.getUUID(), animId.toString());

        PacketDistributor.sendToPlayer(player, packet);
        PacketDistributor.sendToPlayersNear(level, player, player.getX(), player.getY(), player.getZ(), 64.0, packet);

        MultiCastManager.start(player, this);
    }

    @Override
    public void repeatedCast(ServerPlayer player, int iteration) {
        ServerLevel level = player.level();

        Vec3 start = player.getEyePosition().add(0, -0.1, 0);
        Vec3 look = player.getLookAngle();

        Vec3 end = start.add(look.scale(8.0));

        level.playSound(null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.BREEZE_CHARGE,
                SoundSource.PLAYERS,
                1.0f, 1.0f
        );

        HitResult blockHit = level.clip(new ClipContext(
                start,
                end,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        if (blockHit.getType() != HitResult.Type.MISS) {
            end = blockHit.getLocation();
        }

        AABB beamBox = new AABB(start, end).inflate(0.75);

        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                beamBox,
                e -> e.isAlive() && !(e instanceof Player)
        );

        for (LivingEntity entity : targets) {
            if (entity.getBoundingBox().clip(start, end).isPresent()) {

                DamageSource source = level.damageSources().indirectMagic(player, player);

                entity.hurt(source, 19.75f);

                entity.addEffect(new MobEffectInstance(
                        EffectsInit.SHOCKED_EFFECT,
                        200,
                        0
                ));
            }
        }

        spawnBeamParticles(level, start, end);

        if (blockHit.getType() != HitResult.Type.MISS) {
            spawnImpactParticles(level, end);
        }
    }

    private void spawnBeamParticles(ServerLevel level, Vec3 start, Vec3 end) {
        Vec3 dir = end.subtract(start);
        int steps = 12;

        for (int i = 0; i < steps; i++) {
            double t = i / (double) steps;
            Vec3 pos = start.add(dir.scale(t));

            level.sendParticles(
                    ParticleTypes.ELECTRIC_SPARK,
                    pos.x, pos.y, pos.z,
                    3,
                    0.01, 0.01, 0.01,
                    0.05
            );
        }
    }

    private void spawnImpactParticles(ServerLevel level, Vec3 pos) {
        level.sendParticles(
                ParticleTypes.ELECTRIC_SPARK,
                pos.x, pos.y, pos.z,
                50,
                0.5, 0.5, 0.5,
                0.4
        );
    }

    @Override
    public List<SpellInput> getCombo() {
        return List.of(
                SpellInput.B,
                SpellInput.V,
                SpellInput.C
        );
    }

    @Override
    public ResourceLocation getEmptyPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/lightning_beam_empty.png");
    }

    @Override
    public ResourceLocation getFullPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/lightning_beam_full.png");
    }

    @Override
    public ResourceLocation animationId() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "animation.player.cast_lightning_beam");
    }
}
