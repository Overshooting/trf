package com.gmail.aamelis.trf.ModCommands;

import com.gmail.aamelis.trf.ModEntities.NPCs.FlavorNPCEntity;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.NPCArea;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.NPCConstructionHandler;
import com.gmail.aamelis.trf.ModEntities.NPCs.NPCsData.NPCName;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;


public class SummonTestFlavorNPCCommand {

    public static final LiteralArgumentBuilder<CommandSourceStack> SUMMON_TEST_FLAVOR_NPC_COMMAND =
            Commands.literal("summonTestFlavorNPC")
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                .then(Commands.argument("name", StringArgumentType.word())
                                        .suggests((context, builder) ->
                                                SharedSuggestionProvider.suggest(NPCName.getValidNames(), builder))
                                        .then(Commands.argument("location", StringArgumentType.string())
                                                .suggests((context, builder) ->
                                                        SharedSuggestionProvider.suggest(NPCArea.getValidReadableNames(), builder))
                                                .executes(context -> {
                                                    NPCArea location;
                                                    NPCName name;
                                                    int amount = IntegerArgumentType.getInteger(context, "amount");

                                                    name = NPCName.matchNameOrDefault(StringArgumentType.getString(context, "name"), NPCName.DEFAULT);

                                                    switch (StringArgumentType.getString(context, "location")) {
                                                        case ("starter_town") -> location = NPCArea.STARTER_TOWN;
                                                        case ("colonial_town") -> location = NPCArea.COLONIAL_TOWN;

                                                        default -> {
                                                            context.getSource().sendFailure(Component.literal("Invalid location name!"));
                                                            return 0;
                                                        }
                                                    }

                                                    ServerLevel level = context.getSource().getLevel();
                                                    Entity commandEntity = context.getSource().getEntity();

                                                    if (commandEntity == null) {
                                                        context.getSource().sendFailure(Component.literal("Command source was null!"));
                                                        return 0;
                                                    }

                                                    if (!level.isClientSide()) {
                                                        for (int i = 0; i < amount; i++) {
                                                            FlavorNPCEntity thisEntity = NPCConstructionHandler.buildFlavorNPC(location, name, level);
                                                            thisEntity.setPos(commandEntity.getX(), commandEntity.getY(), commandEntity.getZ());

                                                            level.addFreshEntity(thisEntity);
                                                        }
                                                    }

                                                    context.getSource().sendSuccess(() -> Component.literal("Added " + amount + " flavor NPC" + (amount > 1 ? "s" : "") + " named " + name.getName() + " at the location of " + context.getSource().getEntity().getName().getString()), true);
                                                    return amount;
                                                })
                                        )
                                )
                        );

}
