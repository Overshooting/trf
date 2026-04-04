package com.gmail.aamelis.trf.ModSpells.MageSpells;

import com.gmail.aamelis.trf.ModAttachments.PlayerSpellData;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.ModSpells.SpellInput;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ShadowSpell implements ISpell {
    @Override
    public String getId() {
        return "shadow";
    }

    @Override
    public short getRequiredClass() {
        return PlayerSpellData.MAGE;
    }

    @Override
    public int getRequiredMana() {
        return 30;
    }

    @Override
    public void cast(ServerPlayer player) {
        player.level().playSound(null, player.blockPosition(), SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.PLAYERS, 60.0f, 0.8f);
        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 30));
        player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 30, 8));

        Vec3 lookAngle = player.getLookAngle();

        player.setDeltaMovement(lookAngle.x * 2.0, lookAngle.y * 2.0, lookAngle.z * 2.0);

        player.hasImpulse = true;

        player.sendSystemMessage(Component.literal("Shadow cast successfully!"));
    }

    @Override
    public List<SpellInput> getCombo() {
        return List.of(
                SpellInput.V,
                SpellInput.V,
                SpellInput.V
        );
    }
}
