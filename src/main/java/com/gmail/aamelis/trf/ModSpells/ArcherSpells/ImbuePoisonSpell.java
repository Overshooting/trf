package com.gmail.aamelis.trf.ModSpells.ArcherSpells;

import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;
import com.gmail.aamelis.trf.ModEffects.Imbuements.ImbuementEffect;
import com.gmail.aamelis.trf.ModPlayerData.ModStats.PlayerStatData;
import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf.Registries.EffectsInit;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ImbuePoisonSpell implements ISpell {
    @Override
    public String getId() {
        return "imbue_poison";
    }

    @Override
    public String getDisplayName() {
        return "Imbue Poison";
    }

    @Override
    public short getRequiredClass() {
        return PlayerSpellData.ARCHER;
    }

    @Override
    public int getRequiredMana() {
        return 50;
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
        PlayerStatData data = player.getData(AttachmentTypesInit.PLAYER_STATS);

        int duration = 600 - (500 / (data.getPerception() + 1));

        Collection<MobEffectInstance> activeEffects = player.getActiveEffects();
        ArrayList<MobEffectInstance> effectsToRemove = new ArrayList<>();

        for (MobEffectInstance instance : activeEffects) {
            if (instance.getEffect().value() instanceof ImbuementEffect) {
                effectsToRemove.add(instance);
            }
        }

        for (MobEffectInstance effectInstance : effectsToRemove) {
            player.removeEffect(effectInstance.getEffect());
        }

        player.level().playSound(null, player.blockPosition(), SoundEvents.ALLAY_ITEM_GIVEN, SoundSource.PLAYERS, 60.0f, 0.8f);
        player.addEffect(new MobEffectInstance(EffectsInit.POISON_IMBUEMENT_EFFECT, duration));
    }

    @Override
    public void repeatedCast(ServerPlayer player, int iteration) {

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
    public ResourceLocation getFullPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/imbue_poison_full.png");
    }

    @Override
    public ResourceLocation getEmptyPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/imbue_poison_empty.png");
    }

    @Override
    public ResourceLocation animationId() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "animation.player.cast_imbue_poison");
    }
}
