package com.gmail.aamelis.trf.ModEntities.NPCs;

import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.FlavorTextByArea;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.NPCArea;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.NPCName;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AbstractFlavorEntity extends Mob {

    private NPCArea location;
    private NPCName name;

    public AbstractFlavorEntity(EntityType<? extends Mob> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    public AbstractFlavorEntity(EntityType<? extends Mob> entityType, NPCArea location, NPCName name, Level level) {
        this(entityType, level);

        this.location = location;
        this.name = name;
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, Integer.MAX_VALUE)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 8.0D)
                .add(Attributes.WAYPOINT_RECEIVE_RANGE, 8.0D)
                .add(Attributes.WAYPOINT_TRANSMIT_RANGE, 64.0D);
    }

    public void setName(NPCName name) {
        this.name = name;
    }

    public NPCName getNPCName() {
        return name;
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    public String getText() {
        return FlavorTextByArea.getRandomText(location);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new LookAtPlayerGoal(this, Player.class, 8.0f));
    }

    @Override
    public boolean isNoAi() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    @Override
    public void travel(Vec3 travelVector) {

    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!level().isClientSide()) {
            player.displayClientMessage(Component.literal(getNPCName().getName() + ": " + getText()).withStyle(ChatFormatting.GOLD), false);
        }

        return InteractionResult.SUCCESS;
    }
}
