package com.gmail.aamelis.trf.ModEntities.Projectiles;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockTypes;
import net.minecraft.world.level.block.Blocks;

public class ProjectileUtils {

    public static boolean validateBlock(Block block) {
        return block != Blocks.SHORT_GRASS &&
                block != Blocks.TALL_GRASS &&
                block != Blocks.TALL_DRY_GRASS &&
                block != Blocks.SHORT_DRY_GRASS &&
                block != Blocks.SNOW &&
                block != Blocks.SEAGRASS;
    }


}
