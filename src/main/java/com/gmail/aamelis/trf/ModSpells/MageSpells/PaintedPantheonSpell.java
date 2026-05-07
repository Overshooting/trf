package com.gmail.aamelis.trf.ModSpells.MageSpells;

import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;
import com.gmail.aamelis.trf.ModEntities.Projectiles.PaintedPantheonProjectile;
import com.gmail.aamelis.trf.ModEntities.Projectiles.SunlightReachProjectile;
import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.List;

public class PaintedPantheonSpell implements ISpell {
    @Override
    public String getId() {
        return "painted_pantheon";
    }

    @Override
    public String getDisplayName() {
        return "Painted Pantheon";
    }

    @Override
    public short getRequiredClass() {
        return PlayerSpellData.MAGE;
    }

    @Override
    public int getRequiredMana() {
        return 300;
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
        PaintedPantheonProjectile proj = new PaintedPantheonProjectile(player.level(), player);

        proj.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.25f, 0.1f);

        player.level().playSound(null, player.blockPosition(), SoundEvents.BOAT_PADDLE_WATER, SoundSource.PLAYERS, 60.0f, 0.8f);

        player.level().addFreshEntity(proj);
    }

    @Override
    public void repeatedCast(ServerPlayer player, int iteration) {

    }

    @Override
    public List<SpellInput> getCombo() {
        return List.of(
                SpellInput.B,
                SpellInput.V,
                SpellInput.V
        );
    }

    @Override
    public ResourceLocation getFullPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/painted_pantheon_full.png");
    }

    @Override
    public ResourceLocation getEmptyPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/painted_pantheon_empty.png");
    }

    @Override
    public ResourceLocation animationId() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "animation.player.cast_painted_pantheon");
    }
}
