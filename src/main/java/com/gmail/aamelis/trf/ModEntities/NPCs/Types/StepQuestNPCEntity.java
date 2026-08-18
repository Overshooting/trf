package com.gmail.aamelis.trf.ModEntities.NPCs.Types;

import com.gmail.aamelis.trf.ModGlobalData.GlobalQuestData;
import com.gmail.aamelis.trf.ModNPCs.Quests.Objectives.QuestObjective;
import com.gmail.aamelis.trf.ModPlayerData.QuestPlayerData.PlayerQuestData;
import com.gmail.aamelis.trf.ModPlayerData.QuestPlayerData.QuestProgress;
import com.gmail.aamelis.trf.ModNPCs.NPCsData.NPCName;
import com.gmail.aamelis.trf.ModNPCs.Quests.QuestLine;
import com.gmail.aamelis.trf.ModNPCs.Quests.QuestProgressChecker;
import com.gmail.aamelis.trf.ModNPCs.Quests.QuestStage;
import com.gmail.aamelis.trf.ModNPCs.Dialog.DialogScheduler;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf.Registries.QuestsInit;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Arrays;
import java.util.List;

public class StepQuestNPCEntity extends AbstractNPCEntity {

    private static final EntityDataAccessor<String> DATA_QUEST =
            SynchedEntityData.defineId(StepQuestNPCEntity.class, EntityDataSerializers.STRING);

    public ResourceLocation questId;

    public StepQuestNPCEntity(EntityType<? extends Mob> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Override
    public void setName(NPCName name) {
        super.setName(name);

        this.questId = ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, getNPCName().getResourceLocationName());

        if (!level().isClientSide()) {
            this.entityData.set(DATA_QUEST, questId.toString());
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.SUCCESS;
        }

        QuestProgress progress;

        QuestLine questLine = QuestsInit.getQuest(questId);

        boolean firstTime;

        if (questLine.isGlobal()) {
            GlobalQuestData data = serverPlayer.level().getDataStorage().get(GlobalQuestData.TYPE);

            firstTime = !(data.getAllQuestProgress().containsKey(questId));

            System.out.println("Detected first time: " + firstTime);

            progress = data.getQuestProgress(questId);
        } else {
            PlayerQuestData data = serverPlayer.getData(AttachmentTypesInit.PLAYER_QUEST_DATA);

            firstTime = !(data.getAll().containsKey(questId));

            progress = data.getOrCreate(questId);
        }

        int stageIndex = progress.getStage();

        QuestStage stage = questLine.stages().get(stageIndex);

        if (!firstTime) {
            boolean complete = true;
            for (QuestObjective obj : stage.objectives()) {
                if (!obj.isComplete(serverPlayer, progress)) {
                    complete = false;
                    break;
                }
            }

            if (complete) {
                QuestProgressChecker.checkPlayerCompletion(serverPlayer, questId, questLine, progress);
                if (questLine.isGlobal()) {
                    GlobalQuestData data = serverPlayer.level().getDataStorage().get(GlobalQuestData.TYPE);
                    progress = data.getQuestProgress(questId);
                    stage = questLine.stages().get(progress.getStage());
                } else {
                    PlayerQuestData data = serverPlayer.getData(AttachmentTypesInit.PLAYER_QUEST_DATA);
                    progress = data.getOrCreate(questId);
                    stage = questLine.stages().get(progress.getStage());
                }
            }
        }

        stageIndex =  progress.getStage();

        if (stageIndex >= questLine.stages().size() - 1) {
            if (questLine.isRepeatable()) {
                System.out.println("Repeatable reached!");
                if (questLine.isGlobal()) {
                    System.out.println("Global reached!");
                    GlobalQuestData data = serverPlayer.level().getDataStorage().get(GlobalQuestData.TYPE);
                    System.out.println("Wiping global quest progress!");
                    data.wipeQuestProgress(questId);
                } else {
                    PlayerQuestData data = serverPlayer.getData(AttachmentTypesInit.PLAYER_QUEST_DATA);
                    data.wipeQuest(questId);
                }
            }
        }

        System.out.println("Scheduling instanced dialogue");

        List<String> lines = Arrays.stream(stage.dialog().split("\n"))
                .map(line -> getNPCName().getName() + ": " + line)
                .toList();

        DialogScheduler.schedule(serverPlayer, lines);

        return InteractionResult.SUCCESS;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder p_326499_) {
        super.defineSynchedData(p_326499_);
        p_326499_.define(DATA_QUEST, "");
    }

    @Override
    public void addAdditionalSaveData(ValueOutput p_421640_) {
        super.addAdditionalSaveData(p_421640_);

        p_421640_.putString("quest_id", questId.toString());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput p_422339_) {
        super.readAdditionalSaveData(p_422339_);

        var readResult = ResourceLocation.read(p_422339_.getStringOr("quest_id", ""));
        if (readResult.isSuccess()) {
            questId = readResult.getOrThrow();
        } else {
            questId = ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "");
        }
    }
}
