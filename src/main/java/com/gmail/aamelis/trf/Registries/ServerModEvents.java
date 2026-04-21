package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModAttachments.QuestAttachments.PlayerQuestData;
import com.gmail.aamelis.trf.ModAttachments.QuestAttachments.QuestProgress;
import com.gmail.aamelis.trf.ModEntities.NPCs.AbstractNPCEntity;
import com.gmail.aamelis.trf.ModEntities.NPCs.Rendering.Dialog.DialogScheduler;
import com.gmail.aamelis.trf.ModEntities.NPCs.Rendering.NPCModel;
import com.gmail.aamelis.trf.Network.GameMasterButtonHandler;
import com.gmail.aamelis.trf.Network.Packets.*;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber(modid = TRFFinalRegistry.MODID)
public class ServerModEvents {

    @SubscribeEvent
    public static void playerTickEvent(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        player.getData(AttachmentTypesInit.PLAYER_MANA.get()).runManaTick(player);
        DialogScheduler.tick();
    }

    @SubscribeEvent
    public static void playerRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        player.getData(AttachmentTypesInit.PLAYER_MANA.get()).fillMana(player);
    }

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final var registrar = event.registrar("1");

        registrar.playToServer(
                StartGamePacket.TYPE,
                StartGamePacket.STREAM_CODEC,
                GameMasterButtonHandler::handleStartGamePacket
        );

        registrar.playToServer(
                ResetGamePacket.TYPE,
                ResetGamePacket.STREAM_CODEC,
                GameMasterButtonHandler::handleResetGamePacket
        );

        registrar.playToServer(
                SetCornersPacket.TYPE,
                SetCornersPacket.STREAM_CODEC,
                GameMasterButtonHandler::handleSetCornersPacket
        );

        registrar.playToServer(
                SetMessagePacket.TYPE,
                SetMessagePacket.STREAM_CODEC,
                GameMasterButtonHandler::handleSetMessagePacket
        );

        registrar.playToServer(
                OpenLightsOutMenuPacket.TYPE,
                OpenLightsOutMenuPacket.STREAM_CODEC,
                GameMasterButtonHandler::handleOpenLightsOutMenu
        );

        registrar.playToServer(
                BackButtonPacket.TYPE,
                BackButtonPacket.STREAM_CODEC,
                GameMasterButtonHandler::handleBackButtonPacket
        );
    }

    @SubscribeEvent
    public static void onAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(EntitiesInit.FLAVOR_NPC_ENTITY.get(), AbstractNPCEntity.createMobAttributes().build());
        event.put(EntitiesInit.STEP_QUEST_NPC_ENTITY.get(), AbstractNPCEntity.createMobAttributes().build());
        event.put(EntitiesInit.TUTORIAL_STEP_QUEST_NPC_ENTITY.get(), AbstractNPCEntity.createMobAttributes().build());
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(NPCModel.LAYER_LOCATION, NPCModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void livingDeathEvent(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;

        var killedType = event.getEntity().getType();

        PlayerQuestData data = player.getData(AttachmentTypesInit.PLAYER_QUEST_DATA);

        for (var entry : data.getAll().entrySet()) {
            var questId = entry.getKey();
            var progress = entry.getValue();

            var questLine = QuestsInit.QUESTS.get(questId);
            if (questLine == null) continue;

            int stageIndex = progress.getStage();
            if (stageIndex >= questLine.stages().size()) continue;

            var stage = questLine.stages().get(stageIndex);

            for (var obj : stage.objectives()) {
                obj.onKill(player, progress, killedType);
            }
        }
    }

    @SubscribeEvent
    public static void itemPickupEvent(ItemEntityPickupEvent.Post event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;

        var stack = event.getItemEntity().getItem();

        PlayerQuestData data = player.getData(AttachmentTypesInit.PLAYER_QUEST_DATA);

        for (var entry : data.getAll().entrySet()) {
            var questId = entry.getKey();
            var progress = entry.getValue();

            var questLine = QuestsInit.QUESTS.get(questId);
            if (questLine == null) continue;

            int stageIndex = progress.getStage();
            if (stageIndex >= questLine.stages().size()) continue;

            var stage = questLine.stages().get(stageIndex);

            for (var obj : stage.objectives()) {
                obj.onItemPickup(player, progress, stack);
            }
        }
    }

}
