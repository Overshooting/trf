package com.gmail.aamelis.trf.ModCommands;

import com.gmail.aamelis.trf.ModGlobalData.GlobalQuestData;
import com.gmail.aamelis.trf.ModPlayerData.ModStats.Levels.PlayerLevelData;
import com.gmail.aamelis.trf.ModPlayerData.QuestPlayerData.PlayerQuestData;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
            Commands.literal("resetAllQuests")
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                    .then(Commands.argument("targets", EntityArgument.players())
                            .then(Commands.argument("questId", StringArgumentType.string())
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
                    ));

    public static final LiteralArgumentBuilder<CommandSourceStack> RESET_ALL_GLOBAL_QUEST_COMMAND =
            Commands.literal("resetAllGlobalQuests")
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                    .executes(context -> {
                        ServerLevel level = context.getSource().getLevel();
                        GlobalQuestData data = level.getDataStorage().get(GlobalQuestData.TYPE);

                        data.wipeAllQuestProgress();

                        context.getSource().sendSuccess(() -> Component.literal("All global quests reset!"), true);

                        return 1;
                    });

    public static final LiteralArgumentBuilder<CommandSourceStack> RESET_GLOBAL_QUEST_COMMAND =
            Commands.literal("resetAllQuests")
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                            .then(Commands.argument("questId", StringArgumentType.string())
                                    .executes(context -> {
                                        ResourceLocation questId = ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, StringArgumentType.getString(context, "questId"));
                                        ServerLevel level = context.getSource().getLevel();
                                        GlobalQuestData data = level.getDataStorage().get(GlobalQuestData.TYPE);

                                        data.wipeQuestProgress(questId);

                                        context.getSource().sendSuccess(() -> Component.literal("Reset quest " + questId + " for " + " all players"), true);

                                        return 1;
                                    })
                            );

}
