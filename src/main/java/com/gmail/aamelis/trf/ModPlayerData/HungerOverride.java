package com.gmail.aamelis.trf.ModPlayerData;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.LevelEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class HungerOverride {

    public static boolean gameRuleSet = false;

    public static void overrideHunger(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        FoodData data = player.getFoodData();

        data.setFoodLevel(20);
        data.setSaturation(20.0f);

        if (!gameRuleSet && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.level()
                    .getGameRules()
                    .getRule(GameRules.RULE_NATURAL_REGENERATION)
                    .set(false, serverPlayer.level().getServer());
        }
    }

}
