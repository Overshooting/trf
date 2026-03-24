package com.gmail.aamelis.trf.ModCommands;

import com.gmail.aamelis.trf.ModBlocks.LightsOutBlock;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public class PresetLightsOutCommand {

    public static final boolean[][] EASY_PRESET = {
            {false, true, false},
            {true, false, true},
            {false, false, false}
    };
    public static final boolean[][] MED_PRESET = {
            {true, false, false, true, true},
            {true, false, true, true, false},
            {false, true, false, false, true},
            {false, true, false, true, false},
            {true, true, true, true, false}
    };
    public static final boolean[][] HARD_PRESET = {
            {true, false, false, true, false, true, false},
            {false, false, true, false, false, false, false},
            {true, true, false, true, false, false, true},
            {false, true, false, false, true, false, true},
            {true, false, false, true, false, true, true},
            {false, false, false, true, true, false, true},
            {true, false, true, false, false, false, true}
    };

    public static final LiteralArgumentBuilder<CommandSourceStack> LIGHTS_OUT_PRESET_COMMAND =
            Commands.literal("buildLightsOutPreset")
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                    .then(Commands.argument("pos1", BlockPosArgument.blockPos())
                            .then(Commands.argument("pos2", BlockPosArgument.blockPos())
                                    .then(Commands.argument("preset", StringArgumentType.word())
                                            .executes(context -> {
                                                BlockPos pos1 = BlockPosArgument.getLoadedBlockPos(context, "pos1");
                                                BlockPos pos2 = BlockPosArgument.getLoadedBlockPos(context, "pos2");
                                                String presetName = StringArgumentType.getString(context, "preset");

                                                boolean[][] preset;

                                                switch (presetName.toLowerCase()) {
                                                    case "easy" -> preset = EASY_PRESET;
                                                    case "med" -> preset = MED_PRESET;
                                                    case "hard" -> preset = HARD_PRESET;
                                                    default -> {
                                                        context.getSource().sendFailure(Component.literal("Invalid preset"));
                                                        return 0;
                                                    }
                                                }

                                                ServerLevel level = context.getSource().getLevel();

                                                if (pos1.getY() != pos2.getY()) {
                                                    context.getSource().sendFailure(Component.literal("Can only build presets on the same y-level!"));
                                                }

                                                int y = pos1.getY();

                                                int minX = Math.min(pos1.getX(), pos2.getX());
                                                int minZ = Math.min(pos1.getZ(), pos2.getZ());
                                                int maxX = Math.max(pos1.getX(), pos2.getX());
                                                int maxZ = Math.max(pos1.getZ(), pos2.getZ());

                                                int width = maxX - minX + 1;
                                                int depth = maxZ - minZ + 1;

                                                int presetHeight = preset.length;
                                                int presetWidth = preset[0].length;

                                                int applied = 0;

                                                for (int dz = 0; dz < depth; dz++) {
                                                    for (int dx = 0; dx < width; dx++) {
                                                        if (dz >= presetHeight || dx >= presetWidth) continue;

                                                        BlockPos currentPos = new BlockPos(minX + dx, y, minZ + dz);
                                                        BlockState state = level.getBlockState(currentPos);
                                                        
                                                        if (!(state.getBlock() instanceof LightsOutBlock)) continue;

                                                        boolean value = preset[dz][dx];

                                                        BlockState newState = state.setValue(LightsOutBlock.ACTIVATED, value);
                                                        level.setBlock(currentPos, newState, 3);

                                                        applied++;
                                                    }
                                                }

                                                final int realApplied = applied;

                                                context.getSource().sendSuccess(() ->
                                                                Component.literal("Applied preset '" + presetName + "' to " + realApplied + " blocks."),
                                                        true
                                                );

                                                return applied;
                                            }))));

}
