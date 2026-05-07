package com.gmail.aamelis.trf.ModSpells.MageSpells;

import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;
import com.gmail.aamelis.trf.ModEntities.Projectiles.ManaBlastProjectile;
import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.Network.Packets.SpellAnimationPacket;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class ManaBlastSpell implements ISpell {
    @Override
    public String getId() {
        return "mana_blast";
    }

    @Override
    public String getDisplayName() {
        return "Mana Blast";
    }

    @Override
    public short getRequiredClass() {
        return PlayerSpellData.MAGE;
    }

    @Override
    public int getRequiredMana() {
        return 60;
    }

    @Override
    public long getCooldown() {
        return 5000;
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

        Vec3 horizontal = new Vec3(player.getLookAngle().x(), 0, player.getLookAngle().z()).normalize();

        player.setDeltaMovement(player.getDeltaMovement().add(horizontal.scale(0.5)));
        player.hasImpulse = true;

        ManaBlastProjectile proj = new ManaBlastProjectile(level, player);

        SpellAnimationPacket packet = new SpellAnimationPacket(player.getUUID(), animationId().toString());

        PacketDistributor.sendToPlayer(player, packet);
        PacketDistributor.sendToPlayersNear(level, player, player.getX(), player.getY(), player.getZ(), 64.0, packet);

        player.level().playSound(null, player.blockPosition(), SoundEvents.CONDUIT_ACTIVATE, SoundSource.PLAYERS, 60.0f, 0.8f);

        proj.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 2.0f, 0.1f);

        level.addFreshEntity(proj);
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
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/mana_blast_full.png");
    }

    @Override
    public ResourceLocation getEmptyPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/mana_blast_empty.png");
    }

    @Override
    public ResourceLocation animationId() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "animation.player.cast_mana_blast");
    }
}
