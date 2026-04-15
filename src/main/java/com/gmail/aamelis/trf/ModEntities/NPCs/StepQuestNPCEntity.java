package com.gmail.aamelis.trf.ModEntities.NPCs;

import com.gmail.aamelis.trf.ModAttachments.PlayerQuestData;
import com.gmail.aamelis.trf.ModAttachments.PlayerSpellData;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.NPCName;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.Quests.QuestStep;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.checkerframework.checker.units.qual.C;

public class StepQuestNPCEntity extends AbstractNPCEntity {

    private QuestStep[] questTree;
    private boolean talking;

    public StepQuestNPCEntity(EntityType<? extends Mob> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);

        questTree = new QuestStep[0];
    }

    public void setNameAndQuests(NPCName name, QuestStep[] questTree) {
        this.setName(name);

        this.questTree = questTree;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (talking || questTree.length  == 0) {
            return InteractionResult.FAIL;
        } else {
            talking = true;
        }

        PlayerQuestData questData = player.getData(AttachmentTypesInit.PLAYER_QUEST_DATA);

        int stage = 1;

        if (questData.hasQuest(getNPCName())) {
            for (int i = 1; i < questTree.length; i++) {
                if (questData.hasQuest(getNPCName(), questTree[i])) {
                    stage = i;
                }
            }
        }

        if (questData.doingQuest(getNPCName(), questTree[stage])) {
            player.displayClientMessage(Component.literal(getNPCName().getName() + ": " + questTree[0].getStageDialog()).withStyle(ChatFormatting.BOLD), false);
            return InteractionResult.SUCCESS;
        } else if (questData.questFinished(getNPCName(), questTree[stage])) {
            if (stage + 1 == questTree.length - 1) {
                questData.finishNPCQuest(getNPCName());
            } else {
                stage++;
                questData.updateNPCQuest(getNPCName(), questTree[stage]);
            }
        }

        String thisDialogue = questTree[stage].getStageDialog();

        while (thisDialogue.indexOf("\n") > 0) {
            player.displayClientMessage(Component.literal(getNPCName().getName() + ": " + thisDialogue.substring(0, thisDialogue.indexOf("\n"))).withStyle(ChatFormatting.BOLD), false);
            thisDialogue = thisDialogue.substring(thisDialogue.indexOf("\n") + 1);

            try {
                wait(2000);
            } catch (InterruptedException e) {
                System.out.println("Exception caused!");
            }
        }

        talking = false;
        return InteractionResult.SUCCESS;
    }

    public void displayWrongClassText(Player player) {
        player.displayClientMessage(Component.literal(getNPCName().getName() + ": " + questTree[0]).withStyle(ChatFormatting.BOLD), false);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput p_421640_) {
        super.addAdditionalSaveData(p_421640_);

        ValueOutput.ValueOutputList listOutput = p_421640_.childrenList("quest_tree");

        for (QuestStep step : questTree) {
            step.serialize(listOutput.addChild());
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput p_422339_) {
        super.readAdditionalSaveData(p_422339_);

        ValueInput.ValueInputList listInput = p_422339_.childrenListOrEmpty("quest_tree");

        questTree = new QuestStep[listInput.stream().toArray().length];

        for (int i = 0; i < questTree.length; i++) {
            QuestStep step = new QuestStep();

            step.deserialize(listInput.iterator().next());
            questTree[i] = step;
        }
    }
}
