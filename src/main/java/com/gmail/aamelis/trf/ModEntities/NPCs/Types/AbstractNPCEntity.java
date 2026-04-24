package com.gmail.aamelis.trf.ModEntities.NPCs.Types;

import com.gmail.aamelis.trf.ModNPCs.NPCsData.NPCName;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class AbstractNPCEntity extends Mob {

    public static final EntityDataAccessor<String> DATA_TEXTURE =
            SynchedEntityData.defineId(AbstractNPCEntity.class, EntityDataSerializers.STRING);

    public static final EntityDataAccessor<String> DATA_NAME =
            SynchedEntityData.defineId(AbstractNPCEntity.class, EntityDataSerializers.STRING);

    private NPCName name;

    public AbstractNPCEntity(EntityType<? extends Mob> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);

        name = NPCName.DEFAULT;
    }

    public void setName(NPCName name) {
        this.name = name;

        if (!level().isClientSide()) {
            this.entityData.set(DATA_TEXTURE, name.getResourceLocationName());
            this.entityData.set(DATA_NAME, name.getName());
        }
    }

    public NPCName getNPCName() {
        return name;
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, Integer.MAX_VALUE)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 8.0D)
                .add(Attributes.WAYPOINT_RECEIVE_RANGE, 8.0D)
                .add(Attributes.WAYPOINT_TRANSMIT_RANGE, 64.0D);
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
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
    protected void defineSynchedData(SynchedEntityData.Builder p_326499_) {
        super.defineSynchedData(p_326499_);

        p_326499_.define(DATA_TEXTURE, "null");
        p_326499_.define(DATA_NAME, NPCName.DEFAULT.getName());
    }

    @Override
    public void addAdditionalSaveData(ValueOutput p_421640_) {
        super.addAdditionalSaveData(p_421640_);
        p_421640_.putString("npc_name", name.getName());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput p_422339_) {
        super.readAdditionalSaveData(p_422339_);

        NPCName loadedName = NPCName.matchNameOrDefault(p_422339_.getStringOr("npc_name", "null"), NPCName.DEFAULT);

        this.name = loadedName;

        this.entityData.set(DATA_NAME, loadedName.getName());
        this.entityData.set(DATA_TEXTURE, loadedName.getResourceLocationName());
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return true;
    }


}
