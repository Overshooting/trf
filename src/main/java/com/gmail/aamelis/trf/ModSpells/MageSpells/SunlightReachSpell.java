package com.gmail.aamelis.trf.ModSpells.MageSpells;

import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;
import com.gmail.aamelis.trf.ModEntities.Projectiles.SpellProjectiles.SunlightReachProjectile;
import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.List;

public class SunlightReachSpell implements ISpell {
    @Override
    public String getId() {
        return "sunlight_reach";
    }

    @Override
    public String getDisplayName() {
        return "Sunlight Reach";
    }

    @Override
    public short getRequiredClass() {
        return PlayerSpellData.MAGE;
    }

    @Override
    public int getRequiredMana() {
        return 150;
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
        SunlightReachProjectile proj = new SunlightReachProjectile(player.level(), player);

        proj.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 2.5f, 0.1f);

        player.level().playSound(null, player.blockPosition(), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 60.0f, 0.8f);

        player.level().addFreshEntity(proj);
    }

    @Override
    public void repeatedCast(ServerPlayer player, int iteration) {

    }

    @Override
    public List<SpellInput> getCombo() {
        return List.of(
                SpellInput.C,
                SpellInput.V,
                SpellInput.C
        );
    }

    @Override
    public ResourceLocation getFullPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/sunlight_reach_full.png");
    }

    @Override
    public ResourceLocation getEmptyPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/sunlight_reach_empty.png");
    }

    @Override
    public ResourceLocation animationId() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "animation.player.cast_sunlight_reach");
    }
}
