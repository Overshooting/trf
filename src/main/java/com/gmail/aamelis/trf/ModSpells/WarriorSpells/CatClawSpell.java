package com.gmail.aamelis.trf.ModSpells.WarriorSpells;

import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;
import com.gmail.aamelis.trf.ModCastingSystem.MultiStepSpells.MultiCastManager;
import com.gmail.aamelis.trf.ModEntities.Projectiles.DamageUtils;
import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.ModUIRendering.RenderUtils;
import com.gmail.aamelis.trf.Network.Packets.SpellAnimationPacket;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class CatClawSpell implements ISpell {
    @Override
    public String getId() {
        return "cat_claw";
    }

    @Override
    public String getDisplayName() {
        return "Cat Claw";
    }

    @Override
    public short getRequiredClass() {
        return PlayerSpellData.WARRIOR;
    }

    @Override
    public int getRequiredMana() {
        return 300;
    }

    @Override
    public long getCooldown() {
        return 10000;
    }

    @Override
    public int multiCastTicks() {
        return 10;
    }

    @Override
    public int repetitions() {
        return 5;
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

        final double SLASH_RANGE = 3.0;
        final double SLASH_WIDTH = 0.8;

        final double SLASH_HEIGHT = 2.0;

        final double SLASH_ANGLE = 80;

        if (iteration < 4) {
            final float BASE_DAMAGE = 0.9f;

            AABB box = DamageUtils.createSlashBox(
                    player,
                    SLASH_RANGE,
                    SLASH_WIDTH,
                    SLASH_HEIGHT
            );

            ResourceLocation animId;

            if (iteration % 2 == 0) {
                animId = ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "animation.player.cast_cat_claw_1");
            } else {
                animId = ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "animation.player.cast_cat_claw_2");
            }

            SpellAnimationPacket packet = new SpellAnimationPacket(
                    player.getUUID(),
                    animId.toString()
            );

            PacketDistributor.sendToPlayer(player, packet);
            PacketDistributor.sendToPlayersNear(
                    level,
                    player,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    64.0,
                    packet
            );

            DamageUtils.applySlashHurtBox(
                    box,
                    player,
                    level,
                    BASE_DAMAGE,
                    PlayerSpellData.WARRIOR,
                    SLASH_RANGE,
                    SLASH_ANGLE
            );

            RenderUtils.renderSlashParticles(player, level, true);
        } else {
            final float FINAL_DAMAGE = 0.3f;
            final double KNOCKBACK = 3.0f;

            AABB box =  DamageUtils.createSlashBox(
                    player,
                    SLASH_RANGE,
                    SLASH_WIDTH,
                    SLASH_HEIGHT
            );

            SpellAnimationPacket packet = new SpellAnimationPacket(
                    player.getUUID(),
                    animationId().toString()
            );

            PacketDistributor.sendToPlayer(player, packet);
            PacketDistributor.sendToPlayersNear(
                    level,
                    player,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    64.0,
                    packet
            );

            DamageUtils.applySlashHurtBox(
                    box,
                    player,
                    level,
                    FINAL_DAMAGE,
                    PlayerSpellData.WARRIOR,
                    SLASH_RANGE,
                    SLASH_ANGLE,
                    KNOCKBACK
            );

            RenderUtils.renderSlashParticles(player, level, false);
        }
    }

    @Override
    public List<SpellInput> getCombo() {
        return List.of(
                SpellInput.C,
                SpellInput.V,
                SpellInput.B
        );
    }

    @Override
    public ResourceLocation getFullPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/cat_claw_full.png");
    }

    @Override
    public ResourceLocation getEmptyPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/cat_claw_empty.png");
    }

    @Override
    public ResourceLocation animationId() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "animation.player.cast_cat_claw_3");
    }
}
