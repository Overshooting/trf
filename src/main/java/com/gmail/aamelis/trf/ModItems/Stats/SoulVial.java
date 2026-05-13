package com.gmail.aamelis.trf.ModItems.Stats;

import com.gmail.aamelis.trf.ModPlayerData.ModStats.Levels.PlayerLevelData;
import com.gmail.aamelis.trf.ModPlayerData.ModStats.PlayerStatData;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.io.Serial;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SoulVial extends Item {

    public static final Supplier<Properties> PROPERTIES = () ->
        new Item.Properties().stacksTo(16).rarity(Rarity.EPIC);

    public SoulVial(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);

        tooltipAdder.accept(Component.translatable("tooltip.trf.soul_vial_1"));
        tooltipAdder.accept(Component.translatable("tooltip.trf.soul_vial_2"));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.SUCCESS;

        PlayerStatData statData = serverPlayer.getData(AttachmentTypesInit.PLAYER_STATS);
        PlayerLevelData levelData = serverPlayer.getData(AttachmentTypesInit.PLAYER_LEVEL);
        ItemStack stack = player.getItemInHand(hand);
        int totalPoints = statData.getStatTotals();

        if (totalPoints == 0) {
            serverPlayer.sendSystemMessage(Component.literal("No allocated stat points to reset!"));
            return InteractionResult.FAIL;
        }

        level.playSound(null, player.blockPosition(), SoundEvents.COPPER_GOLEM_BECOME_STATUE, SoundSource.PLAYERS, 60.0f, 0.8f);

        statData.resetStats(serverPlayer);
        levelData.awardPoints(totalPoints, serverPlayer);
        stack.shrink(1);

        return InteractionResult.CONSUME;
    }
}
