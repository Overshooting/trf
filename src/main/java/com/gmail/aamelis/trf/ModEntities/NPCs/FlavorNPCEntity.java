package com.gmail.aamelis.trf.ModEntities.NPCs;

import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.FlavorTextByArea;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FlavorNPCEntity extends AbstractNPCEntity {
    public FlavorNPCEntity(EntityType<? extends AbstractNPCEntity> entityType, Level p_20967_) {
        super(entityType, p_20967_);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!level().isClientSide()) {
            player.playSound(SoundEvents.VILLAGER_AMBIENT, 1.0f, 1.0f);
            player.displayClientMessage(Component.literal(getNPCName().getName() + ": " + getText()).withStyle(ChatFormatting.GOLD), false);
        }

        return InteractionResult.SUCCESS;
    }

    public String getText() {
        return FlavorTextByArea.getRandomText(getLocation());
    }
}
