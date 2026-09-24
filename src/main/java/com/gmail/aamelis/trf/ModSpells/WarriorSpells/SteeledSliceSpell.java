package com.gmail.aamelis.trf.ModSpells.WarriorSpells;

import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;
import com.gmail.aamelis.trf.ModItems.Weapons.Warrior.AbstractMeleeItem;
import com.gmail.aamelis.trf.ModItems.Weapons.Warrior.SwordType;
import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

import java.util.List;

public class SteeledSliceSpell implements ISpell {


    @Override
    public String getId() {
        return "Steeled Slice";
    }

    @Override
    public String getDisplayName() {
        return "steeled_slice";
    }

    @Override
    public short getRequiredClass() {
        return PlayerSpellData.WARRIOR;
    }

    @Override
    public int getRequiredMana() {
        return 500;
    }

    @Override
    public long getCooldown() {
        return 10000;
    }

    @Override
    public int multiCastTicks() {
        return 0;
    }

    @Override
    public int repetitions() {
        return 0;
    }

    @Override
    public void cast(ServerPlayer player) {
        Item item = player.getMainHandItem().getItem();

        if (!(item instanceof AbstractMeleeItem abstractMeleeItem)) return;

        switch (abstractMeleeItem.getType()) {

            case SWORD_TYPE:

                break;

            case AXE_TYPE:

                break;

            case DAGGER_TYPE:

                break;

            default:
                throw new IllegalStateException("Unexpected item type: " + abstractMeleeItem.getType());
        }
    }

    @Override
    public void repeatedCast(ServerPlayer player, int iteration) {

    }

    @Override
    public List<SpellInput> getCombo() {
        return List.of(
                SpellInput.V,
                SpellInput.C,
                SpellInput.B
        );
    }

    @Override
    public ResourceLocation getFullPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/steeled_slice_full.png");
    }

    @Override
    public ResourceLocation getEmptyPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/steeled_slice_empty.png");
    }

    @Override
    public ResourceLocation animationId() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "animation.player.cast_steeled_slice_sword");
    }
}
