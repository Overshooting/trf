package com.gmail.aamelis.trf.ModCommands;

import com.gmail.aamelis.trf.ModEntities.NPCs.Types.FlavorNPCEntity;
import com.gmail.aamelis.trf.ModNPCs.NPCsData.NPCArea;
import com.gmail.aamelis.trf.ModNPCs.NPCConstructionHandler;
import com.gmail.aamelis.trf.ModNPCs.NPCsData.NPCName;
import com.gmail.aamelis.trf.ModEntities.NPCs.Types.StepQuestNPCEntity;
import com.gmail.aamelis.trf.ModEntities.NPCs.Types.TutorialStepQuestNPCEntity;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;


public class SummonNPCCommands {

    public static final LiteralArgumentBuilder<CommandSourceStack> SUMMON_FLAVOR_NPC_COMMAND =
            Commands.literal("summonTestFlavorNPC")
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                    .then(Commands.argument("name", StringArgumentType.string())
                                    .suggests((context, builder) ->
                                            SharedSuggestionProvider.suggest(NPCName.getValidFlavorNames(), builder))
                                    .then(Commands.argument("location", StringArgumentType.string())
                                            .suggests((context, builder) ->
                                                    SharedSuggestionProvider.suggest(NPCArea.getValidReadableNames(), builder))
                                            .executes(context -> {
                                                NPCName name = NPCName.matchNameOrDefault(StringArgumentType.getString(context, "name"), NPCName.DEFAULT);
                                                NPCArea location = NPCArea.matchReadableNameOrDefault(StringArgumentType.getString(context, "location"), NPCArea.DEFAULT);

                                                ServerLevel level = context.getSource().getLevel();
                                                Entity commandEntity = context.getSource().getEntity();

                                                if (commandEntity == null) {
                                                    context.getSource().sendFailure(Component.literal("Command source was null!"));
                                                    return 0;
                                                }

                                                if (!level.isClientSide()) {
                                                    FlavorNPCEntity thisEntity = NPCConstructionHandler.buildFlavorNPC(location, name, level);
                                                    thisEntity.setPos(commandEntity.getX(), commandEntity.getY(), commandEntity.getZ());

                                                    level.addFreshEntity(thisEntity);
                                                }

                                                context.getSource().sendSuccess(() -> Component.literal("Added a flavor NPC" + " named " + name.getName() + " at the location of " + context.getSource().getEntity().getName().getString()), true);
                                                return 1;
                                            })
                                    )
                        );

    public static final LiteralArgumentBuilder<CommandSourceStack> SUMMON_STEP_QUEST_NPC_COMMAND =
            Commands.literal("summonStepQuestNPC")
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                    .then(Commands.argument("name", StringArgumentType.greedyString())
                            .suggests((context, builder) ->
                                    SharedSuggestionProvider.suggest(NPCName.getValidQuestNames(), builder))
                                    .executes(context -> {
                                        NPCName name = NPCName.matchNameOrDefault(StringArgumentType.getString(context, "name"), NPCName.DEFAULT);

                                        ServerLevel level = context.getSource().getLevel();
                                        Entity commandEntity = context.getSource().getEntity();

                                        if (commandEntity == null) {
                                            context.getSource().sendFailure(Component.literal("Command source was null!"));
                                            return 0;
                                        }

                                        if (!level.isClientSide()) {
                                            try {
                                                StepQuestNPCEntity thisEntity = NPCConstructionHandler.buildStepQuestNPC(name, level);
                                                thisEntity.setPos(commandEntity.getX(), commandEntity.getY(), commandEntity.getZ());

                                                level.addFreshEntity(thisEntity);
                                            } catch (IllegalStateException e) {
                                                context.getSource().sendFailure(Component.literal("No npc with name " + name + " found!"));
                                                return 0;
                                            }
                                        }

                                        context.getSource().sendSuccess(() -> Component.literal("Added " + name.getName() + " at the location of " + context.getSource().getEntity().getName().getString()), true);
                                        return 1;
                                    })
                            );

    public static final LiteralArgumentBuilder<CommandSourceStack> SUMMON_TUTORIAL_STEP_QUEST_NPC_COMMAND =
            Commands.literal("summonTutorialNPC")
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                    .then(Commands.argument("name", StringArgumentType.greedyString())
                            .suggests((context, builder) ->
                                    SharedSuggestionProvider.suggest(NPCName.getValidTutorialNames(), builder))
                                    .executes(context -> {
                                        NPCName name = NPCName.matchNameOrDefault(StringArgumentType.getString(context, "name"), NPCName.DEFAULT);

                                        ServerLevel level = context.getSource().getLevel();
                                        Entity commandEntity = context.getSource().getEntity();

                                        if (commandEntity == null) {
                                            context.getSource().sendFailure(Component.literal("Command source was null!"));
                                            return 0;
                                        }

                                        if (!level.isClientSide()) {
                                            try {
                                                TutorialStepQuestNPCEntity thisEntity = NPCConstructionHandler.buildTutorialStepQuestNPC(name, level);
                                                thisEntity.setPos(commandEntity.getX(), commandEntity.getY(), commandEntity.getZ());

                                                level.addFreshEntity(thisEntity);
                                            } catch (IllegalStateException e) {
                                                context.getSource().sendFailure(Component.literal("No npc with name " + name + " found!"));
                                                return 0;
                                            }
                                        }

                                        context.getSource().sendSuccess(() -> Component.literal("Added " + name.getName() + " at the location of " + context.getSource().getEntity().getName().getString()), true);
                                        return 1;
                                    })
                    );

}
