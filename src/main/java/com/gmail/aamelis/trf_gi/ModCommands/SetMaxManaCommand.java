package com.gmail.aamelis.trf_gi.ModCommands;

import com.gmail.aamelis.trf_gi.ModAttachments.PlayerMana;
import com.gmail.aamelis.trf_gi.Registries.AttachmentTypesInit;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class SetMaxManaCommand {

    public static final LiteralArgumentBuilder<CommandSourceStack> SET_MAX_MANA_COMMAND =
            Commands.literal("setMaxMana")
                .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                    .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                            .executes(context -> {
                                Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
                                int value = IntegerArgumentType.getInteger(context, "amount");

                                for (ServerPlayer player : targets) {
                                    PlayerMana manaData = player.getData(AttachmentTypesInit.PLAYER_MANA.get());

                                    manaData.setMaxMana(value);
                                }

                                if (targets.size() == 1) {
                                    context.getSource().sendSuccess(() -> Component.literal("Set max mana to: " + value + " for " +
                                            targets.iterator().next().getScoreboardName()), true);
                                } else {
                                    context.getSource().sendSuccess(() -> Component.literal("Set max mana to: " + value + " for " +
                                            targets.size()), true);
                                }

                                return targets.size();
                            })
                    )
                    );

}
