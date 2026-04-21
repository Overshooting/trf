package com.gmail.aamelis.trf.ModEntities.NPCs;

import com.gmail.aamelis.trf.ModAttachments.PlayerSpellData;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.Quests.QuestLine;
import com.gmail.aamelis.trf.ModEntities.NPCs.Rendering.Dialog.DialogScheduler;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf.Registries.QuestsInit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class TutorialStepQuestNPCEntity extends StepQuestNPCEntity {

    private short requiredClass;

    public TutorialStepQuestNPCEntity(EntityType<? extends Mob> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);

        requiredClass = PlayerSpellData.EMPTY;
    }

    public void setRequiredClass(short newClass) {
        requiredClass = newClass;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.SUCCESS;
        }

        PlayerSpellData spellData = player.getData(AttachmentTypesInit.PLAYER_SPELL_DATA);
        QuestLine questLine = QuestsInit.QUESTS.get(questId);

        if (spellData.getPlayerClass() != requiredClass) {
            int delay = 0;

            for (String line : questLine.stages().getLast().dialog().split("\n")) {
                String text = getNPCName().getName() + ": " + line;

                DialogScheduler.schedule(serverPlayer, text, delay);

                delay += 40;
            }
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput p_421640_) {
        super.addAdditionalSaveData(p_421640_);

        p_421640_.putShort("class", requiredClass);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput p_422339_) {
        super.readAdditionalSaveData(p_422339_);

        requiredClass = (short)p_422339_.getShortOr("class", PlayerSpellData.EMPTY);
    }
}
