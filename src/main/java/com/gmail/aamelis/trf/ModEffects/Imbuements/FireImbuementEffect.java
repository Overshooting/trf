package com.gmail.aamelis.trf.ModEffects.Imbuements;

import com.gmail.aamelis.trf.ModPlayerData.ModStats.PlayerStatData;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FireImbuementEffect extends MobEffect implements ImbuementEffect{

    public FireImbuementEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffect(LivingEntity entity, ServerPlayer source) {
        PlayerStatData data = source.getData(AttachmentTypesInit.PLAYER_STATS);

        int duration = 220 - (180 / (data.getPerception() + 1));

        entity.setRemainingFireTicks(duration);
    }
}
