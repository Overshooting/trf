package com.gmail.aamelis.trf.ModEffects.Imbuements;

import com.gmail.aamelis.trf.ModPlayerData.ModStats.PlayerStatData;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.AttachmentType;

public class PoisonImbuementEffect extends MobEffect implements ImbuementEffect {

    private int strength;

    public PoisonImbuementEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public void applyEffect(LivingEntity entity, ServerPlayer source) {
        PlayerStatData data = source.getData(AttachmentTypesInit.PLAYER_STATS);

        int amplifier = data.getPerception() / 50;
        int duration = 220 - (180 / (data.getPerception() + 1));


        if (entity.isInvertedHealAndHarm()) {
            entity.addEffect(new MobEffectInstance(MobEffects.WITHER, duration, amplifier));
        } else {
            entity.addEffect(new MobEffectInstance(MobEffects.POISON, duration, amplifier));
        }
    }
}
