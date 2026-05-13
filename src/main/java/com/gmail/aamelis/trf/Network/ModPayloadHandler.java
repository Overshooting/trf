package com.gmail.aamelis.trf.Network;

import com.gmail.aamelis.trf.ModCastingSystem.Combo.ClientComboState;
import com.gmail.aamelis.trf.ModCastingSystem.ClientCooldownState;
import com.gmail.aamelis.trf.ModCastingSystem.SpellAnimations;
import com.gmail.aamelis.trf.ModCastingSystem.SpellCastingSystem;
import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;
import com.gmail.aamelis.trf.ModPlayerData.ModStats.Levels.PlayerLevelData;
import com.gmail.aamelis.trf.ModPlayerData.ModStats.PlayerStatData;
import com.gmail.aamelis.trf.Network.Packets.*;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ModPayloadHandler {

    public static void handleSpellInput(SpellInputPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer)context.player();

            SpellCastingSystem.handleInput(player, payload.input());
        });
    }

    public static void handleComboFeedback(ComboFeedbackPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (payload.inputs().isEmpty()) {
                ClientComboState.currentInputs.clear();
                ClientComboState.resultState = ClientComboState.NOT_FINISHED;
                return;
            }

            ClientComboState.currentInputs.clear();

            for (int i : payload.inputs()) {
                ClientComboState.currentInputs.add(SpellInput.values()[i]);
            }

            if (payload.finished()) {
                ClientComboState.resultState = payload.success() ? ClientComboState.SUCCESS : ClientComboState.FAILURE;
                ClientComboState.resultTime = System.currentTimeMillis();
            } else {
                if (ClientComboState.resultState != ClientComboState.NOT_FINISHED) {
                    ClientComboState.resultState = ClientComboState.NOT_FINISHED;
                }
            }

            ClientComboState.lastUpdateTime = System.currentTimeMillis();
        });
    }

    public static void handleCooldownSync(CooldownSyncPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientCooldownState.cooldowns.clear();

            for (int i = 0; i < packet.spellIds().size(); i++) {
                ClientCooldownState.cooldowns.put(packet.spellIds().get(i), packet.endTimes().get(i));
            }
        });
    }

    public static void handleAnimation(SpellAnimationPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;

            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) return;

            AbstractClientPlayer target = (AbstractClientPlayer) level.getPlayerByUUID(packet.playerId());
            if (target == null) return;

            PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(target, SpellAnimations.SPELL_LAYER_ID);

            if (controller != null) {
                ResourceLocation animId = ResourceLocation.parse(packet.animationId());
                controller.triggerAnimation(animId);
            }
        });

    }

    public static void handleStatIncrease(StatIncreasePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
           if (!(context.player() instanceof ServerPlayer serverPlayer)) return;

           PlayerLevelData levelData = serverPlayer.getData(AttachmentTypesInit.PLAYER_LEVEL);
           levelData.usePoint(serverPlayer);

           PlayerStatData statData = serverPlayer.getData(AttachmentTypesInit.PLAYER_STATS);

           String type = packet.statType();

           switch (type) {
               case "str" -> statData.incrementStrength(1, serverPlayer);
               case "con" -> statData.incrementConstitution(1, serverPlayer);
               case "man" -> statData.incrementMana(1, serverPlayer);
               case "mag" -> statData.incrementMagic(1, serverPlayer);
               case "per" -> statData.incrementPerception(1, serverPlayer);
               case "pie" -> statData.incrementPiety(1, serverPlayer);
               default -> throw new IllegalArgumentException("Illegal packet of type StatIncreasePacket recieved!");
           }
        });
    }

}
