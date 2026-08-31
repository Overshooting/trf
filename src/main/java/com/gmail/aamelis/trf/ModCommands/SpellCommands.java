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
                                            SharedSuggestionProvider.suggest(SpellsInit.getAllSpellsForClass(context.getSource().getPlayer().getData(AttachmentTypesInit.PLAYER_SPELL_DATA).getPlayerClass()), builder)))
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

    public static final LiteralArgumentBuilder<CommandSourceStack> GIVE_ALL_SPELLS_COMMAND =
            Commands.literal("unlockAllSpells")
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                    .then(Commands.argument("targets", EntityArgument.players())
                                    .executes(context -> {
                                        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");

                                        for (ServerPlayer player : targets) {
                                            PlayerSpellData data = player.getData(AttachmentTypesInit.PLAYER_SPELL_DATA);

                                            data.unlockAllSpells(player);
                                        }

                                        if (targets.size() == 1) {
                                            context.getSource().sendSuccess(() -> Component.literal("Unlocked all spells for " +
                                                    targets.iterator().next().getScoreboardName()), true);
                                        } else {
                                            context.getSource().sendSuccess(() -> Component.literal("Unlocked all spells for " +
                                                    targets.size() + " players"), true);
                                        }

                                        return targets.size();
                                    })
                    );

    public static final LiteralArgumentBuilder<CommandSourceStack> REVOKE_SPELL_COMMAND =
            Commands.literal("revokeSpell")
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                    .then(Commands.argument("targets", EntityArgument.players())
                            .then(Commands.argument("spell", StringArgumentType.greedyString())
                                    .suggests(((context, builder) ->
                                            SharedSuggestionProvider.suggest(SpellsInit.getAllSpellsForClass(context.getSource().getPlayer().getData(AttachmentTypesInit.PLAYER_SPELL_DATA).getPlayerClass()), builder)))
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

    public static final LiteralArgumentBuilder<CommandSourceStack> REVOKE_ALL_SPELLS_COMMAND =
            Commands.literal("revokeAllSpells")
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                    .then(Commands.argument("targets", EntityArgument.players())
                            .executes(context -> {
                                Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");

                                for (ServerPlayer player : targets) {
                                    PlayerSpellData data = player.getData(AttachmentTypesInit.PLAYER_SPELL_DATA);

                                    data.revokeAllSpells(player);
                                }

                                if (targets.size() == 1) {
                                    context.getSource().sendSuccess(() -> Component.literal("Revoked all spells for " +
                                            targets.iterator().next().getScoreboardName()), true);
                                } else {
                                    context.getSource().sendSuccess(() -> Component.literal("Revoked all spells for " +
                                            targets.size() + " players"), true);
                                }

                                return targets.size();
                            })
                    );

    public static final LiteralArgumentBuilder<CommandSourceStack> ACTIVATE_SPELL_COMMAND =
            Commands.literal("activateSpell")
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                    .then(Commands.argument("targets", EntityArgument.players())
                            .then(Commands.argument("spell", StringArgumentType.greedyString())
                                    .suggests(((context, builder) ->
                                            SharedSuggestionProvider.suggest(SpellsInit.getAllSpellsForClass(context.getSource().getPlayer().getData(AttachmentTypesInit.PLAYER_SPELL_DATA).getPlayerClass()), builder)))
                                    .executes(context -> {
                                        String spellName = StringArgumentType.getString(context, "spell");

                                        if (!SpellsInit.getAllSpellNames().contains(spellName)) {
                                            context.getSource().sendFailure(Component.literal("No spell with the name of " + spellName + " found!"));
                                            return 0;
                                        }

                                        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
                                        String spellId = spellName.replaceAll(" ", "_").toLowerCase();
                                        int successCount = 0, failCount = 0;

                                        for (ServerPlayer player : targets) {
                                            PlayerSpellData data = player.getData(AttachmentTypesInit.PLAYER_SPELL_DATA);

                                            boolean added = data.tryActivateSpell(player, spellId);

                                            if (added) {
                                                successCount++;
                                            } else {
                                                failCount++;
                                            }
                                        }

                                        final int finalSuccessCount = successCount;
                                        final int finalFailCount = failCount;
                                        if (finalSuccessCount > 0) {
                                            context.getSource().sendSuccess(() -> Component.literal("Successfully activated spell for " + finalSuccessCount + " players, failed to activate spell for " + finalFailCount + " players" ), true);
                                        } else {
                                            context.getSource().sendFailure(Component.literal("Failed to activate spell for " + finalFailCount + " players"));
                                        }

                                        return targets.size();
                                    })
                            )
                    );

    public static final LiteralArgumentBuilder<CommandSourceStack> DEACTIVATE_ALL_SPELLS_COMMAND =
            Commands.literal("deactivateAllSpell")
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                    .then(Commands.argument("targets", EntityArgument.players())
                                    .executes(context -> {
                                        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
                                        int revoked = 0;

                                        for (ServerPlayer player : targets) {
                                            PlayerSpellData data = player.getData(AttachmentTypesInit.PLAYER_SPELL_DATA);

                                            revoked += data.deactivateAllSpells(player) ? 1 : 0;
                                        }

                                        final int printed = revoked;
                                        if (printed == 1) {
                                            context.getSource().sendSuccess(() -> Component.literal("Deactivated all spells for " +
                                                    targets.iterator().next().getScoreboardName()), true);
                                        } else {
                                            context.getSource().sendSuccess(() -> Component.literal("Deactivated all spells for " +
                                                    printed + " players"), true);
                                        }

                                        return printed;
                                    })
                    );

    public static final LiteralArgumentBuilder<CommandSourceStack> DEACTIVATE_SPELL_COMMAND =
            Commands.literal("deactivateSpell")
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                    .then(Commands.argument("targets", EntityArgument.players())
                            .then(Commands.argument("spell", StringArgumentType.greedyString())
                                    .suggests(((context, builder) ->
                                            SharedSuggestionProvider.suggest(context.getSource().getPlayer().getData(AttachmentTypesInit.PLAYER_SPELL_DATA).getActiveSpells(), builder)))
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

                                            revoked += data.deactivateSpell(player, spellId) ? 1 : 0;
                                        }

                                        final int printed = revoked;
                                        if (printed == 1) {
                                            context.getSource().sendSuccess(() -> Component.literal("Deactivated spell " + spellName + " for " +
                                                    targets.iterator().next().getScoreboardName()), true);
                                        } else {
                                            context.getSource().sendSuccess(() -> Component.literal("Deactivated spell " + spellName + " for " +
                                                    printed + " players"), true);
                                        }

                                        return printed;
                                    })
                            )
                    );

}
