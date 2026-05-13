package com.gmail.aamelis.trf.ModCommands;

import com.gmail.aamelis.trf.ModPlayerData.ModStats.Levels.PlayerLevelData;
import com.gmail.aamelis.trf.ModPlayerData.QuestPlayerData.PlayerQuestData;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class ResetQuestCommand {

    public static final LiteralArgumentBuilder<CommandSourceStack> RESET_QUEST_COMMAND =
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

}
