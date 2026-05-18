package com.gmail.aamelis.trf.ModSpells.ArcherSpells;

import com.gmail.aamelis.trf.ModCastingSystem.Keybinds.SpellInput;
import com.gmail.aamelis.trf.ModItems.DataComponents.BowCastingData;
import com.gmail.aamelis.trf.ModItems.Weapons.Ranger.AbstractModBowItem;
import com.gmail.aamelis.trf.ModPlayerData.PlayerSpellData;
import com.gmail.aamelis.trf.ModSpells.ISpell;
import com.gmail.aamelis.trf.Registries.AttachmentTypesInit;
import com.gmail.aamelis.trf.Registries.DataComponentsInit;
import com.gmail.aamelis.trf.TRFFinalRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PiercingShotSpell implements ISpell {
    @Override
    public String getId() {
        return "piercing_shot";
    }

    @Override
    public String getDisplayName() {
        return "Piercing Shot";
    }

    @Override
    public short getRequiredClass() {
        return PlayerSpellData.ARCHER;
    }

    @Override
    public int getRequiredMana() {
        return 100;
    }

    @Override
    public long getCooldown() {
        return 5000;
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
        ItemStack bow = player.getMainHandItem();

        if (!(bow.getItem() instanceof AbstractModBowItem)) return;

        bow.set(DataComponentsInit.BOW_DATA, new BowCastingData(System.currentTimeMillis(), BowCastingData.PIERCING));
    }

    @Override
    public void repeatedCast(ServerPlayer player, int iteration) {

    }

    @Override
    public List<SpellInput> getCombo() {
        return List.of(
                SpellInput.C,
                SpellInput.C,
                SpellInput.B
        );
    }

    @Override
    public ResourceLocation getFullPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/piercing_shot_full.png");
    }

    @Override
    public ResourceLocation getEmptyPath() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "textures/gui/cooldowns/piercing_shot_empty.png");
    }

    @Override
    public ResourceLocation animationId() {
        return ResourceLocation.fromNamespaceAndPath(TRFFinalRegistry.MODID, "unreachable");
    }
}
