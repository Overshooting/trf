package com.gmail.aamelis.trf.ModSpells.MageSpells;

import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;
import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.Network.Packets.LightningBeamPacket;
import com.gmail.aamelis.trf.Registries.EffectsInit;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

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
        return 20000;
    }

    @Override
    public void cast(ServerPlayer player) {
        ServerLevel level = player.level();

        Vec3 start = player.getEyePosition();
        Vec3 look = player.getLookAngle();

        double maxDistance = 10.0;
        Vec3 end = start.add(look.scale(maxDistance));

        HitResult hit = level.clip(new ClipContext(
                start,
                end,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        if (hit.getType() != HitResult.Type.MISS) {
            end = hit.getLocation();
        }

        Vec3 impact = end;

        AABB box = new AABB(start, end).inflate(0.75);

        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, box, e -> e instanceof Monster && e.isAlive());

        for (LivingEntity entity : targets) {
            Vec3 beamVec = end.subtract(start);
            Vec3 toEntity = entity.position().subtract(start);

            double t = toEntity.dot(beamVec) / beamVec.lengthSqr();
            t = Math.clamp(t, 0.0, 1.0);

            Vec3 closest = start.add(beamVec.scale(t));

            if (closest.distanceToSqr(entity.position()) < 1.5) {
                entity.hurt(player.damageSources().magic(), 6.0f);

                entity.addEffect(new MobEffectInstance(
                        EffectsInit.SHOCKED_EFFECT,
                        100,
                        0
                ));
            }
        }

        PacketDistributor.sendToPlayersTrackingEntityAndSelf(
                player,
                new LightningBeamPacket(start, end, impact)
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
