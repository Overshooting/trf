package com.gmail.aamelis.trf.ModItems.Weapons.Warrior;

import com.gmail.aamelis.trf.ModCastingSystem.DelayedEffects.DelayedSpellEffect;
import com.gmail.aamelis.trf.ModCastingSystem.DelayedEffects.DelayedSpellEffectScheduler;
import com.gmail.aamelis.trf.Network.Packets.SpellAnimationPacket;
import com.gmail.aamelis.trf.Registries.EffectsInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Collection;

abstract class AbstractMeleeItem extends Item {

    public AbstractMeleeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!(player instanceof ServerPlayer serverPlayer && level instanceof ServerLevel serverLevel)) return InteractionResult.SUCCESS;


        boolean isCooldown = serverPlayer.getCooldowns().isOnCooldown(serverPlayer.getItemInHand(hand));

        if (isCooldown) {
            return InteractionResult.PASS;
        } else {
            System.out.println("Parry initiated!");

            SpellAnimationPacket packet = new SpellAnimationPacket(player.getUUID(), animId().toString());

            PacketDistributor.sendToPlayer(serverPlayer, packet);
            PacketDistributor.sendToPlayersNear(serverLevel, serverPlayer, player.getX(), player.getY(), player.getZ(), 64.0, packet);

            serverPlayer.addEffect(new MobEffectInstance(EffectsInit.PARRYING_EFFECT, 30, 1));

            DelayedSpellEffectScheduler.schedule(serverLevel, new DelayedSpellEffect(30, (lvl) -> {
                System.out.println("Parry Window Closed!");

                Collection<MobEffectInstance> effects = serverPlayer.getActiveEffects();
                ArrayList<MobEffectInstance> effectsToRemove = new ArrayList<MobEffectInstance>();
                for (MobEffectInstance effect : effects) {
                    if (effect.getEffect() == EffectsInit.PARRYING_EFFECT) {
                        effectsToRemove.add(effect);
                    }
                }

                for (MobEffectInstance effect : effectsToRemove) {
                    serverPlayer.removeEffect(effect.getEffect());

                    serverPlayer.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 40, 255));
                    serverPlayer.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 255));
                    serverPlayer.getCooldowns().addCooldown(serverPlayer.getItemInHand(hand), 40);
                }
            }
                    ));

            return InteractionResult.SUCCESS;
        }
    }

    abstract ResourceLocation animId();
}
