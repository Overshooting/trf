package com.gmail.aamelis.trf.ModCommands;

import com.gmail.aamelis.trf.ModGlobalData.GlobalQuestData;
import com.gmail.aamelis.trf.ModGlobalData.GlobalRewardData;
import com.gmail.aamelis.trf.ModNPCs.NPCsData.NPCName;
import com.gmail.aamelis.trf.ModNPCs.Quests.QuestLine;
import com.gmail.aamelis.trf.ModNPCs.Quests.QuestStage;
import com.gmail.aamelis.trf.ModPlayerData.ModStats.Levels.PlayerLevelData;
import com.gmail.aamelis.trf.ModPlayerData.QuestPlayerData.PlayerQuestData;
import com.gmail.aamelis.trf.ModPlayerData.QuestPlayerData.PlayerRewardData;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf.Registries.QuestsInit;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.Configuration;

import java.util.Collection;
import java.util.List;

public class ResetQuestCommand {

    public static final LiteralArgumentBuilder<CommandSourceStack> RESET_ALL_PLAYER_QUEST_COMMAND =
            Commands.literal("resetAllQuests")
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                    .then(Commands.argument("targets", EntityArgument.players())
                            .executes(context -> {
                                Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");

                                for (ServerPlayer player : targets) {
                                    PlayerQuestData questData = player.getData(AttachmentTypesInit.PLAYER_QUEST_DATA);

                                    questData.wipeAllQuestProgress();
                                }

                                if (targets.size() == 1) {
                                    context.getSource().sendSuccess(() -> Component.literal("Reset quests for " +
                                            targets.iterator().next().getScoreboardName()), true);
                                } else {
                                    context.getSource().sendSuccess(() -> Component.literal("Reset quests for " +
                                            targets.size() + " targets"), true);
                                }

                                return targets.size();
                            })
                    );

    public static final LiteralArgumentBuilder<CommandSourceStack> RESET_PLAYER_QUEST_COMMAND =
            Commands.literal("resetQuest")
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                    .then(Commands.argument("targets", EntityArgument.players())
                            .then(Commands.argument("questId", StringArgumentType.string())
                                    .suggests((context, builder) ->
                                            SharedSuggestionProvider.suggest(NPCName.getPlayerQuestIds(), builder))
                            .executes(context -> {
                                Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
                                ResourceLocation questId = ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, StringArgumentType.getString(context, "questId"));

                                for (ServerPlayer player : targets) {
                                    PlayerQuestData questData = player.getData(AttachmentTypesInit.PLAYER_QUEST_DATA);

                                    questData.wipeQuest(questId);
                                }

                                if (targets.size() == 1) {
                                    context.getSource().sendSuccess(() -> Component.literal("Reset quest " + questId + " for " +
                                            targets.iterator().next().getScoreboardName()), true);
                                } else {
                                    context.getSource().sendSuccess(() -> Component.literal("Reset quest " + questId + " for " +
                                            targets.size() + " targets"), true);
                                }

                                return targets.size();
                            })
                        )
                    );

    public static final LiteralArgumentBuilder<CommandSourceStack> RESET_ALL_GLOBAL_QUEST_COMMAND =
            Commands.literal("resetAllGlobalQuests")
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                    .executes(context -> {
                        ServerLevel level = context.getSource().getLevel();
                        GlobalQuestData data = GlobalQuestData.getGlobalQuestData(level.getServer());
                        GlobalRewardData rewardData = GlobalRewardData.getGlobalRewardData(level.getServer());

                        data.wipeAllQuestProgress();
                        rewardData.removeAllRewards();

                        for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
                            PlayerRewardData playerRewardData = player.getData(AttachmentTypesInit.PLAYER_REWARD_DATA);

                            playerRewardData.removeAllRewards(player);
                        }

                        context.getSource().sendSuccess(() -> Component.literal("All global quests reset!"), true);

                        return 1;
                    });

    public static final LiteralArgumentBuilder<CommandSourceStack> RESET_GLOBAL_QUEST_COMMAND =
            Commands.literal("resetGlobalQuest")
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                            .then(Commands.argument("questId", StringArgumentType.string())
                                    .suggests((context, builder) ->
                                            SharedSuggestionProvider.suggest(NPCName.getGlobalQuestIds(), builder))
                                    .executes(context -> {
                                        ResourceLocation questId = ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, StringArgumentType.getString(context, "questId"));
                                        ServerLevel level = context.getSource().getLevel();
                                        GlobalQuestData data = GlobalQuestData.getGlobalQuestData(level.getServer());
                                        GlobalRewardData rewardData = GlobalRewardData.getGlobalRewardData(level.getServer());

                                        QuestLine line = QuestsInit.getQuest(questId);

                                        data.wipeQuestProgress(questId);
                                        for (QuestStage stage : line.stages()) {
                                            int experience = stage.experience();
                                            ItemStack item = stage.rewardItem();

                                            rewardData.removeRewards(experience, item);
                                        }

                                        for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
                                            PlayerRewardData playerRewardData = player.getData(AttachmentTypesInit.PLAYER_REWARD_DATA);

                                            for (QuestStage stage : line.stages()) {
                                                int experience = stage.experience();
                                                ItemStack item = stage.rewardItem();

                                                playerRewardData.removeRewards(experience, item, player);
                                            }
                                        }

                                        context.getSource().sendSuccess(() -> Component.literal("Reset quest " + questId + " for all players"), true);

                                        return 1;
                                    })
                            );

}
