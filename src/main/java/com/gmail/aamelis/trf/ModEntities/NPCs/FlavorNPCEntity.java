package com.gmail.aamelis.trf.ModEntities.NPCs;

import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.FlavorTextByArea;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.NPCArea;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.NPCName;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class FlavorNPCEntity extends AbstractNPCEntity {

    public static final EntityDataAccessor<String> DATA_LOCATION =
            SynchedEntityData.defineId(FlavorNPCEntity.class, EntityDataSerializers.STRING);

    private NPCArea location;

    public FlavorNPCEntity(EntityType<? extends AbstractNPCEntity> entityType, Level p_20967_) {
        super(entityType, p_20967_);

        location = NPCArea.DEFAULT;
    }

    public void setLocation(NPCArea location) {
        this.location = location;
    }

    public NPCArea getLocation() {
        return location;
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

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder p_326499_) {
        super.defineSynchedData(p_326499_);

        p_326499_.define(DATA_LOCATION, NPCArea.DEFAULT.getReadableName());
    }

    @Override
    public void addAdditionalSaveData(ValueOutput p_421640_) {
        super.addAdditionalSaveData(p_421640_);
        p_421640_.putString("npc_area", location.getReadableName());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput p_422339_) {
        super.readAdditionalSaveData(p_422339_);

        NPCArea loadedArea = NPCArea.matchReadableNameOrDefault(p_422339_.getStringOr("npc_area", "null"), NPCArea.DEFAULT);

        this.location = loadedArea;

        this.entityData.set(DATA_LOCATION, loadedArea.getReadableName());
    }
}
