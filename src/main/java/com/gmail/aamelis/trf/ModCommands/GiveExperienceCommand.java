package com.gmail.aamelis.trf.ModCommands;

import com.gmail.aamelis.trf.ModPlayerData.ModStats.Levels.PlayerLevelData;
import com.gmail.aamelis.trf.ModPlayerData.PlayerMana;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class GiveExperienceCommand {

    public static final LiteralArgumentBuilder<CommandSourceStack> GIVE_EXPERIENCE_COMMAND =
            Commands.literal("giveExperience")
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                    .then(Commands.argument("targets", EntityArgument.players())
                            .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                    .executes(context -> {
                                        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
                                        int value = IntegerArgumentType.getInteger(context, "amount");

                                        for (ServerPlayer player : targets) {
                                            PlayerLevelData levelData = player.getData(AttachmentTypesInit.PLAYER_LEVEL);

                                            levelData.addExperience(value, player);
                                        }

                                        if (targets.size() == 1) {
                                            context.getSource().sendSuccess(() -> Component.literal("Gave " + value + " experience to " +
                                                    targets.iterator().next().getScoreboardName()), true);
                                        } else {
                                            context.getSource().sendSuccess(() -> Component.literal("Gave " + value + " experience " +
                                                    targets.size() + " targets"), true);
                                        }

                                        return targets.size();
                                    })
                            )
                    );

}
