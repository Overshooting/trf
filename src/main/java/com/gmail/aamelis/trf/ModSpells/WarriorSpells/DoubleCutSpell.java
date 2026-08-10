package com.gmail.aamelis.trf.ModSpells.WarriorSpells;

import com.gmail.aamelis.trf.ModCastingSystem.DelayedEffects.DelayedSpellEffect;
import com.gmail.aamelis.trf.ModCastingSystem.DelayedEffects.DelayedSpellEffectScheduler;
import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;
import com.gmail.aamelis.trf.ModEntities.Projectiles.DamageUtils;
import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.Network.Packets.SpellAnimationPacket;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class DoubleCutSpell implements ISpell {
    @Override
    public String getId() {
        return "double_cut";
    }

    @Override
    public String getDisplayName() {
        return "double_cut";
    }

    @Override
    public short getRequiredClass() {
        return PlayerSpellData.WARRIOR;
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
        final double FIRST_SLASH_RANGE = 2.0;
        final double SECOND_SLASH_RANGE = 4.0;

        final double FIRST_SLASH_WIDTH = 0.75;
        final double SECOND_SLASH_WIDTH = 1.0;

        final double SLASH_HEIGHT = 2.0;

        final double SLASH_ANGLE = 60.0;

        final float BASE_DAMAGE = 1.75f;

        ServerLevel level = player.level();

        AABB firstBox = DamageUtils.createSlashBox(
                player,
                FIRST_SLASH_RANGE,
                FIRST_SLASH_WIDTH,
                SLASH_HEIGHT
        );

        SpellAnimationPacket firstPacket = new SpellAnimationPacket(
                player.getUUID(),
                animationId().toString()
        );

        PacketDistributor.sendToPlayer(player, firstPacket);
        PacketDistributor.sendToPlayersNear(
                level,
                player,
                player.getX(),
                player.getY(),
                player.getZ(),
                64.0,
                firstPacket
        );

        DamageUtils.applySlashHurtBox(
                firstBox,
                player,
                level,
                BASE_DAMAGE,
                PlayerSpellData.WARRIOR,
                FIRST_SLASH_RANGE,
                SLASH_ANGLE
        );

        Vec3 eyePos = player.getEyePosition();
        Vec3 lookAngle = player.getLookAngle();

        double slashX = eyePos.x + lookAngle.x;
        double slashY = player.getY() + player.getEyeHeight() - 0.5;
        double slashZ = eyePos.z + lookAngle.z;

        double xOffset = 0.0;
        double zOffset = 0.0;

        if (lookAngle.z > lookAngle.x) {
            zOffset = ((int)(Math.random() * 2) == 0 ? -1 : 1) *(Math.random() * 3) + 1;
        } else {
            xOffset = ((int)(Math.random() * 2) == 0 ? -1 : 1) *(Math.random() * 3) + 1;
        }

        level.sendParticles(
                ParticleTypes.CRIMSON_SPORE,
                slashX,
                slashY,
                slashZ,
                60,
                xOffset,
                0.0,
                zOffset,
                0.0
        );

        DelayedSpellEffectScheduler.schedule(
                level,
                new DelayedSpellEffect(10, lvl -> {

                    AABB secondBox = DamageUtils.createSlashBox(
                            player,
                            SECOND_SLASH_RANGE,
                            SECOND_SLASH_WIDTH,
                            SLASH_HEIGHT
                    );

                    SpellAnimationPacket secondPacket =
                            new SpellAnimationPacket(
                                    player.getUUID(),
                                    ResourceLocation.fromNamespaceAndPath(
                                            TRFFinalRegistry.MODID,
                                            "animation.player.cast_double_cut_2"
                                    ).toString()
                            );

                    PacketDistributor.sendToPlayer(player, secondPacket);
                    PacketDistributor.sendToPlayersNear(
                            level,
                            player,
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            64.0,
                            secondPacket
                    );

                    DamageUtils.applySlashHurtBox(
                            secondBox,
                            player,
                            level,
                            BASE_DAMAGE,
                            PlayerSpellData.WARRIOR,
                            SECOND_SLASH_RANGE,
                            SLASH_ANGLE
                    );

                    lvl.sendParticles(
                            ParticleTypes.CRIMSON_SPORE,
                            slashX,
                            slashY,
                            slashZ,
                            40,
                            0.0,
                            (Math.random() * 2) + 1,
                            0.0,
                            0.0
                    );
                })
        );
    }

    @Override
    public void repeatedCast(ServerPlayer player, int iteration) {

    }

    @Override
    public List<SpellInput> getCombo() {
        return List.of(
                SpellInput.C,
                SpellInput.C,
                SpellInput.C
        );
    }

    @Override
    public ResourceLocation getFullPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/double_cut_full.png");
    }

    @Override
    public ResourceLocation getEmptyPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/double_cut_empty.png");
    }

    @Override
    public ResourceLocation animationId() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "animation.player.cast_double_cut_1");
    }
}
