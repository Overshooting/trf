package com.gmail.aamelis.trf.ModCommands;

import com.gmail.aamelis.trf.ModAttachments.PlayerSpellData;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class SetClassCommand {

    public static final LiteralArgumentBuilder<CommandSourceStack> SET_CLASS_COMMAND =
            Commands.literal("setClass")
                    .requires(stack -> stack.hasPermission(2))
                    .then(Commands.argument("targets", EntityArgument.players())
                            .then(Commands.argument("class", StringArgumentType.word())
                                    .executes(context -> {
                                        String className = StringArgumentType.getString(context, "class");
                                        short classNumber = 0;

                                        switch (className.toLowerCase()) {
                                            case "warrior" -> classNumber = 1;
                                            case "mage" -> classNumber = 2;
                                            case "archer" -> classNumber = 3;
                                            case "cleric" -> classNumber = 4;
                                            default -> {
                                                context.getSource().sendFailure(Component.literal("Invalid class name. Valid class names are: warrior, mage, archer, and cleric."));
                                                return 0;
                                            }
                                        }

                                        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");

                                        for (ServerPlayer player : targets) {
                                            PlayerSpellData playerData = player.getData(AttachmentTypesInit.PLAYER_SPELL_DATA);

                                            playerData.setPlayerClass(classNumber, player);
                                        }

                                        if (targets.size() == 1) {
                                            context.getSource().sendSuccess(() -> Component.literal("Set class to " + className + " for " +
                                                    targets.iterator().next().getScoreboardName()), true);
                                        } else {
                                            context.getSource().sendSuccess(() -> Component.literal("Set class to " + className + " for " +
                                                    targets.size() + " targets"), true);
                                        }

                                        return targets.size();
                                    })));


}
