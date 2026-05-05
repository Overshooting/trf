package com.gmail.aamelis.trf.ModItems.Scrolls;

import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf.Registries.SpellsInit;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class AbstractScrollItem extends Item {

    public static final Supplier<Properties> SCROLL_PROPERTIES = () ->
            new Item.Properties().stacksTo(1);

    private String spellId;

    public AbstractScrollItem(Properties properties, String spellId) {
        this(properties);

        this.spellId = spellId;
    }

    public AbstractScrollItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.PASS;
        }

        ItemStack itemStack = player.getItemInHand(hand);
        PlayerSpellData data = serverPlayer.getData(AttachmentTypesInit.PLAYER_SPELL_DATA);

        ISpell spell = SpellsInit.get(spellId);

        if (spell == null) {
            itemStack.shrink(1);
            return InteractionResult.CONSUME;
        }

        if (data.hasSpell(spellId)) {
            serverPlayer.sendSystemMessage(Component.literal("Spell has already been unlocked"));
            return InteractionResult.FAIL;
        }

        if (spell.getRequiredClass() != data.getPlayerClass()) {
            serverPlayer.sendSystemMessage(Component.literal("Incompatible class for this spell!"));
            return InteractionResult.FAIL;
        }

        data.unlockSpell(spellId, serverPlayer);

        itemStack.shrink(1);

        serverPlayer.sendSystemMessage(Component.literal("Unlocked spell: " + spell.getDisplayName()));

        return InteractionResult.CONSUME;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);

        ISpell spell = SpellsInit.get(spellId);

        if (spell != null) {
            String className = switch(spell.getRequiredClass()) {
                case PlayerSpellData.WARRIOR -> "WARRIOR";
                case PlayerSpellData.MAGE ->  "MAGE";
                case PlayerSpellData.ARCHER -> "ARCHER";
                case PlayerSpellData.CLERIC ->  "CLERIC";
                default -> "None";
            };

            tooltipAdder.accept(Component.literal(className));
        } else {
            tooltipAdder.accept(Component.literal("Invalid Spell"));
        }
    }
}
