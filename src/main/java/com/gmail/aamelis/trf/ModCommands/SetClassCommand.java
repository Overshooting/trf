package com.gmail.aamelis.trf.ModCommands;

import com.gmail.aamelis.trf.ModAttachments.PlayerSpellData;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.List;

public class SetClassCommand {

    public static final LiteralArgumentBuilder<CommandSourceStack> SET_CLASS_COMMAND =
            Commands.literal("setClass")
                    .requires(stack -> stack.hasPermission(2))
                    .then(Commands.argument("targets", EntityArgument.players())
                            .then(Commands.argument("class", StringArgumentType.word())
                                    .suggests((context, builder) ->
                                            SharedSuggestionProvider.suggest(PlayerSpellData.getAllClassStrings(), builder))
                                    .executes(context -> {
                                        String className = StringArgumentType.getString(context, "class");
                                        short classNumber = getClassNumber(className);

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

    private static short getClassNumber(String className) {
        short classNumber = PlayerSpellData.EMPTY;

        switch (className.toLowerCase()) {
            case "warrior" -> classNumber = PlayerSpellData.WARRIOR;
            case "mage" -> classNumber = PlayerSpellData.MAGE;
            case "archer" -> classNumber = PlayerSpellData.ARCHER;
            case "cleric" -> classNumber = PlayerSpellData.CLERIC;
        }
        return classNumber;
    }


}
