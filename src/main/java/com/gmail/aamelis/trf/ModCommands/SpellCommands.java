package com.gmail.aamelis.trf.ModCommands;

import com.gmail.aamelis.trf.ModPlayerData.PlayerMana;
import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf.Registries.SpellsInit;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class SpellCommands {

    public static final LiteralArgumentBuilder<CommandSourceStack> GIVE_SPELL_COMMAND =
            Commands.literal("unlockSpell")
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                    .then(Commands.argument("targets", EntityArgument.players())
                            .then(Commands.argument("spell", StringArgumentType.greedyString())
                                    .suggests(((context, builder) ->
                                            SharedSuggestionProvider.suggest(SpellsInit.getAllSpellNames(), builder)))
                                    .executes(context -> {
                                        String spellName = StringArgumentType.getString(context, "spell");
                                        if (!SpellsInit.getAllSpellNames().contains(spellName)) {
                                            context.getSource().sendFailure(Component.literal("No spell with the name of " + spellName + " found!"));
                                            return 0;
                                        }

                                        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
                                        String spellId = spellName.replaceAll(" ", "_").toLowerCase();

                                        for (ServerPlayer player : targets) {
                                            PlayerSpellData data = player.getData(AttachmentTypesInit.PLAYER_SPELL_DATA);

                                            data.unlockSpell(spellId, player);
                                        }

                                        if (targets.size() == 1) {
                                            context.getSource().sendSuccess(() -> Component.literal("Unlocked spell " + spellName + " for " +
                                                    targets.iterator().next().getScoreboardName()), true);
                                        } else {
                                            context.getSource().sendSuccess(() -> Component.literal("Unlocked spell " + spellName + " for " +
                                                    targets.size() + " players"), true);
                                        }

                                        return targets.size();
                                    })
                            )
                    );

    public static final LiteralArgumentBuilder<CommandSourceStack> REVOKE_SPELL_COMMAND =
            Commands.literal("revokeSpell")
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                    .then(Commands.argument("targets", EntityArgument.players())
                            .then(Commands.argument("spell", StringArgumentType.greedyString())
                                    .suggests(((context, builder) ->
                                            SharedSuggestionProvider.suggest(SpellsInit.getAllSpellNames(), builder)))
                                    .executes(context -> {
                                        String spellName = StringArgumentType.getString(context, "spell");
                                        if (!SpellsInit.getAllSpellNames().contains(spellName)) {
                                            context.getSource().sendFailure(Component.literal("No spell with the name of " + spellName + " found!"));
                                            return 0;
                                        }

                                        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
                                        String spellId = spellName.replaceAll(" ", "_").toLowerCase();
                                        int revoked = 0;

                                        for (ServerPlayer player : targets) {
                                            PlayerSpellData data = player.getData(AttachmentTypesInit.PLAYER_SPELL_DATA);

                                            revoked += data.revokeSpell(spellId, player) ? 1 : 0;
                                        }

                                        final int printed = revoked;

                                        if (printed == 1) {
                                            context.getSource().sendSuccess(() -> Component.literal("Revoked spell " + spellName + " for " +
                                                    targets.iterator().next().getScoreboardName()), true);
                                        } else {
                                            context.getSource().sendSuccess(() -> Component.literal("Unlocked spell " + spellName + " for " +
                                                    printed + " players"), true);
                                        }

                                        return printed;
                                    })
                            )
                    );

}
