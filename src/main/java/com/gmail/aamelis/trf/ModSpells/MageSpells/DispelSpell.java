package com.gmail.aamelis.trf.ModSpells.MageSpells;

import com.gmail.aamelis.trf.ModAttachments.PlayerSpellData;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.Quests.QuestTriggerSystem;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.ModSpells.SpellInput;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DispelSpell implements ISpell {
    @Override
    public String getId() {
        return "dispel";
    }

    @Override
    public short getRequiredClass() {
        return PlayerSpellData.MAGE;
    }

    @Override
    public int getRequiredMana() {
        return 50;
    }

    @Override
    public void cast(ServerPlayer player) {
        Collection<MobEffectInstance> activeEffects = player.getActiveEffects();
        ArrayList<MobEffectInstance> effectsToRemove = new ArrayList<>();
        int effectsRemoved = 0;

        for (MobEffectInstance instance : activeEffects) {
            if (instance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
                effectsToRemove.add(instance);
            }
        }

        for (MobEffectInstance instance : effectsToRemove) {
            player.removeEffect(instance.getEffect());
            effectsRemoved++;
        }

        Level level = player.level();

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0f, 1.0f);

        player.sendSystemMessage(Component.literal(effectsRemoved + " harmful effects removed by Dispel!").withStyle(
                effectsRemoved > 0 ? ChatFormatting.GOLD : ChatFormatting.DARK_GRAY));
    }

    @Override
    public List<SpellInput> getCombo() {
        return List.of(
                SpellInput.V,
                SpellInput.C,
                SpellInput.C
        );
    }
}
