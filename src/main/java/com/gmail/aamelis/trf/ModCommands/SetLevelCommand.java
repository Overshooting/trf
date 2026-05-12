package com.gmail.aamelis.trf.ModCommands;

import com.gmail.aamelis.trf.ModPlayerData.ModStats.Levels.PlayerLevelData;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class SetLevelCommand {

    public static final LiteralArgumentBuilder<CommandSourceStack> SET_LEVEL_COMMAND =
            Commands.literal("setLevel")
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                    .then(Commands.argument("targets", EntityArgument.players())
                            .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                    .executes(context -> {
                                        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
                                        int value = IntegerArgumentType.getInteger(context, "amount");

                                        for (ServerPlayer player : targets) {
                                            PlayerLevelData levelData = player.getData(AttachmentTypesInit.PLAYER_LEVEL);

                                            levelData.setLevel(value, player);
                                        }

                                        if (targets.size() == 1) {
                                            context.getSource().sendSuccess(() -> Component.literal("Set level to " + value + " for " +
                                                    targets.iterator().next().getScoreboardName()), true);
                                        } else {
                                            context.getSource().sendSuccess(() -> Component.literal("Set level to " + value + " for " +
                                                    targets.size() + " targets"), true);
                                        }

                                        return targets.size();
                                    })
                            )
                    );

}
