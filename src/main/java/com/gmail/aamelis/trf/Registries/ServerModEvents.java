package com.gmail.aamelis.trf.Registries;

import com.gmail.aamelis.trf.ModCastingSystem.SpellCastingSystem;
import com.gmail.aamelis.trf.ModEntities.NPCs.Types.AbstractNPCEntity;
import com.gmail.aamelis.trf.ModNPCs.DataLoaders.QuestDataLoader;
import com.gmail.aamelis.trf.ModNPCs.Dialog.DialogScheduler;
import com.gmail.aamelis.trf.ModEntities.NPCs.Rendering.NPCModel;
import com.gmail.aamelis.trf.ModNPCs.Quests.Objectives.ItemObjective;
import com.gmail.aamelis.trf.ModNPCs.Quests.Objectives.KillObjective;
import com.gmail.aamelis.trf.ModPlayerData.HungerOverride;
import com.gmail.aamelis.trf.Network.GameMasterButtonHandler;
import com.gmail.aamelis.trf.Network.Packets.*;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = TRFFinalRegistry.MODID)
public class ServerModEvents {

    @SubscribeEvent
    public static void playerTickEvent(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        player.getData(AttachmentTypesInit.PLAYER_MANA.get()).runManaTick(player);

        DialogScheduler.tick();

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
    }

    @SubscribeEvent
    public static void itemPickupEvent(ItemEntityPickupEvent.Post event) {
        ItemObjective.itemPickupEvent(event);
    }

    @SubscribeEvent
    public static void onAddReloadListener(AddServerReloadListenersEvent event) {
        DataLoadersInit.register(event);
    }

}
