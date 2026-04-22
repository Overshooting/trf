package com.gmail.aamelis.trf.ModEntities.NPCs;

import com.gmail.aamelis.trf.ModAttachments.QuestAttachments.PlayerQuestData;
import com.gmail.aamelis.trf.ModAttachments.QuestAttachments.QuestProgress;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.NPCName;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.Quests.QuestLine;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.Quests.QuestProgressChecker;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.Quests.QuestStage;
import com.gmail.aamelis.trf.ModEntities.NPCs.Rendering.Dialog.DialogScheduler;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf.Registries.QuestsInit;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
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

        this.questId = ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, getNPCName().name().toLowerCase());

        if (!level().isClientSide()) {
            this.entityData.set(DATA_QUEST, questId.toString());
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.SUCCESS;
        }

        PlayerQuestData data = serverPlayer.getData(AttachmentTypesInit.PLAYER_QUEST_DATA);

        QuestLine questLine = QuestsInit.getQuest(questId);
        QuestProgress progress = data.getOrCreate(questId);

        int stageIndex = progress.getStage();

        int delay = 0;

        if (stageIndex >= questLine.stages().size()) {
            for (String line : questLine.stages().getLast().dialog().split("\n")) {
                String text = getNPCName().getName() + ": " + line;

                DialogScheduler.schedule(serverPlayer, text, delay);

                delay += 40;
            }
            return InteractionResult.SUCCESS;
        }

        QuestStage stage = questLine.stages().get(stageIndex);

        boolean complete = true;
        for (var obj : stage.objectives()) {
            if (!obj.isComplete(serverPlayer, progress)) {
                complete = false;
                break;
            }
        }

        if (complete) {
            QuestProgressChecker.checkCompletion(serverPlayer, questId, questLine, progress);
            data = serverPlayer.getData(AttachmentTypesInit.PLAYER_QUEST_DATA);
            progress = data.getOrCreate(questId);
            stage = questLine.stages().get(progress.getStage());
        }

        for (String line : stage.dialog().split("\n")) {
            String text = getNPCName().getName() + ": " + line;

            DialogScheduler.schedule(serverPlayer, text, delay);

            delay += 40;
        }

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
