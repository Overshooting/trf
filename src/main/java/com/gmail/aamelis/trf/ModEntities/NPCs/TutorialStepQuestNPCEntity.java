package com.gmail.aamelis.trf.ModEntities.NPCs;

import com.gmail.aamelis.trf.ModAttachments.PlayerSpellData;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
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
        PlayerSpellData spellData = player.getData(AttachmentTypesInit.PLAYER_SPELL_DATA);

        if (spellData.getPlayerClass() != requiredClass) {
            super.displayWrongClassText(player);
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

        p_422339_.getShortOr("class", PlayerSpellData.EMPTY);
    }
}
