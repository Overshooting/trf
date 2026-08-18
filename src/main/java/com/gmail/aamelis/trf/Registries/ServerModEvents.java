package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModCastingSystem.DelayedEffects.DelayedSpellEffectScheduler;
import com.gmail.aamelis.trf.ModCastingSystem.MultiStepSpells.MultiCastManager;
import com.gmail.aamelis.trf.ModCastingSystem.SpellCastingSystem;
import com.gmail.aamelis.trf.ModNPCs.Dialog.DialogScheduler;
import com.gmail.aamelis.trf.ModNPCs.Quests.Objectives.ItemObjective;
import com.gmail.aamelis.trf.ModNPCs.Quests.Objectives.KillObjective;
import com.gmail.aamelis.trf.ModPlayerData.HungerOverride;
import com.gmail.aamelis.trf.ModPlayerData.ModStats.Levels.PlayerLevelData;
import com.gmail.aamelis.trf.ModPlayerData.PlayerParryingData;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = TRFFinalRegistry.MODID)
public class ServerModEvents {

    public static final String GRASS_GIVEN = "grass_given";

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        MultiCastManager.tick();

        for (ServerLevel level : event.getServer().getAllLevels()) {
            DelayedSpellEffectScheduler.tick(level);
        }
    }

    public static void handleBroadcast(ServerPlayer player, String broadcast) {
        switch (broadcast) {
            case GRASS_GIVEN -> player.sendSystemMessage(Component.literal("Grass Given"));
        }
    }

    @SubscribeEvent
    public static void onIncomingLivingHurt(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && event.getSource().is(DamageTypes.MOB_ATTACK)) {
            PlayerParryingData data = player.getData(AttachmentTypesInit.PARRYING_DATA);

            Entity entity = event.getSource().getEntity();

            if (entity instanceof LivingEntity livingEntity) {
                data.handleDamage(event, livingEntity, player);
            }
        }
    }

    @SubscribeEvent
    public static void playerTickEvent(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        player.getData(AttachmentTypesInit.PLAYER_MANA.get()).runManaTick(player);

        player.getData(AttachmentTypesInit.PARRYING_DATA.get()).tick(player);

        DialogScheduler.tick(player.level());

        HungerOverride.overrideHunger(event);

        SpellCastingSystem.onPlayerTick(event);
    }

    @SubscribeEvent
    public static void onAttributeCreation(EntityAttributeCreationEvent event) {
        AttributesInit.register(event);
    }

    @SubscribeEvent
    public static void playerRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        player.getData(AttachmentTypesInit.PLAYER_MANA.get()).fillMana(player);
    }

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        PacketsInit.register(registrar);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        RenderersInit.registerLayers(event);
    }

    @SubscribeEvent
    public static void livingDeathEvent(LivingDeathEvent event) {
        KillObjective.livingDeathEvent(event);

        LivingEntity entity = event.getEntity();
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;

        PlayerLevelData data = player.getData(AttachmentTypesInit.PLAYER_LEVEL);

        int exp = entity.getExperienceReward(player.level(), event.getSource().getEntity());

        data.adjustVanillaExp(exp, player);
    }

    @SubscribeEvent
    public static void itemPickupEvent(ItemEntityPickupEvent.Post event) {
        ItemObjective.itemPickupEvent(event);
    }

    @SubscribeEvent
    public static void onAddReloadListener(AddServerReloadListenersEvent event) {
        DataLoadersInit.register(event);
    }

    @SubscribeEvent
    public static void onServerStartedEvent(ServerStartedEvent event) {
        SpellCastingSystem.populateClassItems();
    }

}
