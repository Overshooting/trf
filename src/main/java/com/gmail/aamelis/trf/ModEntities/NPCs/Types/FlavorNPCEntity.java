package com.gmail.aamelis.trf.ModEntities.NPCs.Types;

import com.gmail.aamelis.trf.ModNPCs.DataLoaders.FlavorTextLoader;
import com.gmail.aamelis.trf.ModNPCs.NPCsData.NPCArea;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.List;

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
        if (!level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.playSound(SoundEvents.VILLAGER_AMBIENT, 1.0f, 1.0f);
            serverPlayer.sendSystemMessage(Component.literal(getNPCName().getName() + ": " + getText()).withStyle(ChatFormatting.GOLD), false);
        }

        return InteractionResult.SUCCESS;
    }

    public String getText() {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, location.getReadableName());

        List<String> texts = FlavorTextLoader.TEXTS.get(id);

        if (texts == null || texts.isEmpty()) {
            return (random.nextInt(5)) > 2 ? "我が刀は研ぎ澄まされ、心に微塵の迷いなし。" : "They are watching...";
        }

        return texts.get(random.nextInt(texts.size()));
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
