package com.gmail.aamelis.trf.ModItems.Weapons.Warrior;

import com.gmail.aamelis.trf.ModCastingSystem.DelayedEffects.DelayedSpellEffect;
import com.gmail.aamelis.trf.ModCastingSystem.DelayedEffects.DelayedSpellEffectScheduler;
import com.gmail.aamelis.trf.ModPlayerData.PlayerParryingData;
import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
import com.gmail.aamelis.trf.Network.Packets.SpellAnimationPacket;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
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

        PlayerSpellData data = player.getData(AttachmentTypesInit.PLAYER_SPELL_DATA);
        PlayerParryingData parryingData = player.getData(AttachmentTypesInit.PARRYING_DATA);

        if (data.getPlayerClass() != PlayerSpellData.WARRIOR) return InteractionResult.FAIL;

        boolean isCooldown = serverPlayer.getCooldowns().isOnCooldown(serverPlayer.getItemInHand(hand));
        boolean isParrying = parryingData.isParrying();

        if (isCooldown || isParrying) {
            System.out.println("Failing");

            return InteractionResult.FAIL;
        } else {
            System.out.println("Parry initiated!");

            parryingData.setParryingTicks(25, serverPlayer);

            SpellAnimationPacket packet = new SpellAnimationPacket(player.getUUID(), animId().toString());

            PacketDistributor.sendToPlayer(serverPlayer, packet);
            PacketDistributor.sendToPlayersNear(serverLevel, serverPlayer, player.getX(), player.getY(), player.getZ(), 64.0, packet);

            return InteractionResult.SUCCESS;
        }
    }

    abstract ResourceLocation animId();
}
