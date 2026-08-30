package com.gmail.aamelis.trf.ModPlayerData.QuestPlayerData;

import com.gmail.aamelis.trf.ModGlobalData.GlobalRewardData;
import com.gmail.aamelis.trf.ModNPCs.DataLoaders.QuestCodecs;
import com.gmail.aamelis.trf.ModNPCs.Quests.QuestStage;
import com.gmail.aamelis.trf.ModPlayerData.ModStats.Levels.PlayerLevelData;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PlayerRewardData {

    private int experience;
    private final Map<Item, Integer> rewards;

    public PlayerRewardData() {
        this(0, new HashMap<>());
    }

    public PlayerRewardData(int exp, Map<Item, Integer> map) {
        experience = exp;
        rewards = new HashMap<>(map);
    }

    public Map<Item, Integer> getItemRewards() {
        return rewards;
    }

    public void matchRewards(ServerPlayer player) {
        MinecraftServer server = player.level().getServer();
        GlobalRewardData data = GlobalRewardData.getGlobalRewardData(server);
        int globalExp = data.getExperience();
        Map<Item, Integer> itemRewardSet = data.getItemRewardSet();

        PlayerLevelData statData = player.getData(AttachmentTypesInit.PLAYER_LEVEL);

        if (experience <= globalExp) {
            statData.addExperience((globalExp - experience), player);

            this.setExperience(globalExp, player);
        }

        for (Map.Entry<Item, Integer> entry : itemRewardSet.entrySet()) {
            PlayerRewardData rewardData = player.getData(AttachmentTypesInit.PLAYER_REWARD_DATA);

            Map<Item, Integer> playerRewards = rewardData.getItemRewards();

            int playerAmount = playerRewards.get(entry.getKey());

            if (playerAmount < entry.getValue()) {
                ItemStack item = new ItemStack(entry.getKey(), (entry.getValue() - playerAmount));

                boolean added = player.getInventory().add(item);

                if (!added) player.drop(item, false);

                rewardData.incrementRewards(0, item, player);
            }
        }
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience, ServerPlayer player) {
        this.experience = experience;

        player.setData(AttachmentTypesInit.PLAYER_REWARD_DATA, new PlayerRewardData(experience, rewards));
    }

    public void incrementRewards(int experience, ItemStack stack, ServerPlayer player) {
        if (experience > 0) {
            this.experience += experience;
        }

        if (stack != null) {
            rewards.merge(stack.getItem(), stack.getCount(), Integer::sum);
        }

        player.setData(AttachmentTypesInit.PLAYER_REWARD_DATA, new PlayerRewardData(experience, rewards));
    }

    public void removeRewards(int experience, ItemStack stack, ServerPlayer player) {
        rewards.merge(stack.getItem(), -1 * stack.getCount(), Integer::sum);

        if (rewards.get(stack.getItem()) <= 0) {
            rewards.remove(stack.getItem());
        }

        if (experience > 0) {
            this.experience -= experience;
        }

        player.setData(AttachmentTypesInit.PLAYER_REWARD_DATA, new PlayerRewardData(experience, rewards));
    }

    public void removeAllRewards(ServerPlayer player) {
        experience = 0;
        rewards.clear();

        player.setData(AttachmentTypesInit.PLAYER_REWARD_DATA, new PlayerRewardData(experience, rewards));
    }

    public static final MapCodec<PlayerRewardData> CODEC = RecordCodecBuilder.mapCodec(playerRewardDataInstance ->
            playerRewardDataInstance.group(
                    Codec.INT.fieldOf("experience")
                                    .forGetter(PlayerRewardData::getExperience),
                    Codec.unboundedMap(QuestCodecs.ITEM_NAME_CODEC.codec(), Codec.INT)
                            .fieldOf("rewards")
                            .forGetter(data -> data.rewards)
            ).apply(playerRewardDataInstance, PlayerRewardData::new)
    );

}
