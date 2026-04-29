package com.gmail.aamelis.trf.ModSpells.MageSpells;

import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;
import com.gmail.aamelis.trf.ModEntities.Projectiles.LightningBeamProjectile;
import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
import com.gmail.aamelis.trf.ModSpells.ISpell;
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
        return 2000;
    }

    @Override
    public void cast(ServerPlayer player) {
        ServerLevel level = player.level();

        for (int i = 0; i < 3; i++) {
            int delay = i * 5;

            level.getServer().addTickable(new Runnable() {
                int ticks = delay;

                @Override
                public void run() {
                    if (ticks-- > 0) {
                        level.getServer().addTickable(this);
                        return;
                    }

                    spawnBeam(player);
                }
            });
        }
    }

    private void spawnBeam(ServerPlayer player) {
        ServerLevel level = player.level();

        LightningBeamProjectile proj = new LightningBeamProjectile(level, player);

        Vec3 look = player.getLookAngle();

        proj.shoot(
                look.x(),
                look.y(),
                look.z(),
                4.0f,
                0.0f
        );

        level.addFreshEntity(proj);
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
