package com.gmail.aamelis.trf.ModPlayerData;

import com.gmail.aamelis.trf.ModCastingSystem.SpellCastingSystem;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf.Registries.ItemsInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.List;

public class PlayerParryingData {

    private int parryingTicks;

    public PlayerParryingData() {
        parryingTicks = 0;
    }

    public PlayerParryingData(int parryingTicks) {
        this.parryingTicks = parryingTicks;
    }

    public void setParryingTicks(int parryingTicks, ServerPlayer player) {
        if (this.parryingTicks <= 0) {
            this.parryingTicks = parryingTicks;

            setDirty(player);
        }
    }

    public int getParryingTicks() {
        return parryingTicks;
    }

    public boolean isParrying() {
        return parryingTicks > 0;
    }

    public void tick(ServerPlayer player) {
        if (parryingTicks > 1) {
            parryingTicks--;

            setDirty(player);
        } else if (parryingTicks == 1) {
            parryingTicks = 0;

            player.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 30, 255));
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 30, 255));

            List<Item> warriorItems = SpellCastingSystem.getItemsForClass(PlayerSpellData.WARRIOR);

            for (Item item : warriorItems) {
                player.getCooldowns().addCooldown(new ItemStack(item), 30);
            }

            setDirty(player);
        } else if (parryingTicks < 0) {
            parryingTicks = 0;

            setDirty(player);
        }
    }

    public void handleDamage(LivingIncomingDamageEvent event, LivingEntity damaging, ServerPlayer player) {
        if (parryingTicks > 0) {
            player.setDeltaMovement(new Vec3(0, 0, 0));

            event.setCanceled(true);

            damaging.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 30, 255));
            damaging.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 30, 255));

            damaging.hurt(damaging.damageSources().playerAttack(player), 0.0f);

            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS, 20.0f, 1.0f);

            parryingTicks = 0;
        }

        setDirty(player);
    }

    private void setDirty(ServerPlayer player) {
        player.setData(AttachmentTypesInit.PARRYING_DATA.get(), new PlayerParryingData(parryingTicks));
    }

    public static final MapCodec<PlayerParryingData> CODEC =
            RecordCodecBuilder.mapCodec(playerParryingDataInstance ->
                    playerParryingDataInstance.group(
                            Codec.INT.fieldOf("ticks")
                                    .forGetter(PlayerParryingData::getParryingTicks)
                    ).apply(playerParryingDataInstance, PlayerParryingData::new)
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerParryingData> STREAM_CODEC =
            StreamCodec.of(
                    (buf, parrying) -> {
                        buf.writeInt(parrying.getParryingTicks());
                    },
                    (buf) -> new  PlayerParryingData(buf.readInt())
            );

}
