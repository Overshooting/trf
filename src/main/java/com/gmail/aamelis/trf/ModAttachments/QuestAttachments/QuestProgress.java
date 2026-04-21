package com.gmail.aamelis.trf.ModAttachments.QuestAttachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class QuestProgress {

    private int stage;
    private final Map<EntityType<?>, Integer> killCounts;
    private final Map<Item, Integer> itemCounts;
    private final Map<String, Integer> triggerCounts;

    public QuestProgress() {
        this(0, new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    public QuestProgress(int stage, Map<EntityType<?>, Integer> killCounts, Map<Item, Integer> itemCounts, Map<String, Integer> triggerCounts) {
        this.stage = stage;
        this.killCounts = killCounts;
        this.itemCounts = itemCounts;
        this.triggerCounts = triggerCounts;
    }

    public int getStage() {
        return stage;
    }

    public void advanceStage() {
        stage++;
    }

    public int getKillCount(EntityType<?> type) {
        return killCounts.getOrDefault(type, 0);
    }

    public void incrementKill(EntityType<?> type) {
        killCounts.merge(type, 1, Integer::sum);
    }

    public int getItemCount(Item item) {
        return itemCounts.getOrDefault(item, 0);
    }

    public void incrementItem(ItemStack stack) {
        itemCounts.merge(stack.getItem(), stack.getCount(), Integer::sum);
    }

    public void incrementTrigger(String id) {
        triggerCounts.merge(id, 1, Integer::sum);
    }

    public int getTriggerCount(String id) {
        return triggerCounts.getOrDefault(id, 0);
    }

    public static final MapCodec<QuestProgress> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
                Codec.INT.fieldOf("stage").forGetter(q -> q.stage),
                Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT)
                        .fieldOf("kills")
                        .forGetter(q -> {
                            Map<ResourceLocation, Integer> map = new HashMap<>();
                            for (var e : q.killCounts.entrySet()) {
                                map.put(BuiltInRegistries.ENTITY_TYPE.getKey(e.getKey()), e.getValue());
                            }
                            return map;
                        }),
                Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT)
                                .fieldOf("items")
                                        .forGetter(q -> {
                                            Map<ResourceLocation, Integer> map = new HashMap<>();
                                            for (var e : q.itemCounts.entrySet()) {
                                                map.put(BuiltInRegistries.ITEM.getKey(e.getKey()), e.getValue());
                                            }
                                            return map;
                                        }),
                Codec.unboundedMap(Codec.STRING, Codec.INT)
                        .fieldOf("triggers")
                        .forGetter(q -> q.triggerCounts)
        ).apply(instance, (stage, kills, items, triggerCounts) -> {
            Map<EntityType<?>, Integer> converted = new HashMap<>();
            for (var e : kills.entrySet()) {
                Optional<Holder.Reference<EntityType<?>>> thisEntityType = BuiltInRegistries.ENTITY_TYPE.get(e.getKey());
                if (thisEntityType.isEmpty()) continue;

                converted.put(thisEntityType.get().value(), e.getValue());
            }

            Map<Item, Integer> convertedItems = new HashMap<>();
            for (var e : items.entrySet()) {
                var holder = BuiltInRegistries.ITEM.get(e.getKey());
                if (holder.isEmpty()) continue;

                convertedItems.put(holder.get().value(), e.getValue());
            }
            return new QuestProgress(stage, converted, convertedItems, triggerCounts);
        })
    );

}
